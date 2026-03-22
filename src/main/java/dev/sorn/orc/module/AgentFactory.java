package dev.sorn.orc.module;

import dev.sorn.orc.types.AgentData;
import dev.sorn.orc.types.AgentDefinition;
import dev.sorn.orc.types.AgentRole;
import dev.sorn.orc.types.AgentTrigger;
import dev.sorn.orc.types.BddInstruction;
import dev.sorn.orc.types.Id;
import dev.sorn.orc.types.WorkflowDefinition;
import io.vavr.collection.List;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;

import static dev.sorn.orc.json.Json.fromJson;
import static io.vavr.collection.List.ofAll;

public class AgentFactory {

    public List<AgentDefinition> loadFromJson(String json) {
        final var root = fromJson(json);
        final var agentsNode = (ArrayNode) root.get("agents");
        return ofAll(agentsNode).map(this::toAgentDefinition);
    }

    public List<WorkflowDefinition> loadWorkflowsFromJson(String json) {
        final var root = fromJson(json);
        final var workflowsNode = root.get("workflows");
        if (workflowsNode == null || !workflowsNode.isArray()) {
            return List.empty();
        }
        return ofAll(workflowsNode).map(this::toWorkflowDefinition);
    }

    private AgentDefinition toAgentDefinition(JsonNode node) {
        final var id = Id.of(node.get("id").asText());
        final var role = AgentRole.valueOf(node.get("role").asText().toUpperCase());
        final var toolIds = ofAll(node.get("toolIds")).map(t -> Id.of(t.asText()));
        final var inputs = parseAgentData(node.get("input"));
        final var outputs = parseAgentData(node.get("output"));
        final var instructions = parseInstructions(node.get("instructions"));
        final var triggers = parseTriggers(node.get("triggers"));
        final var modelId = node.has("modelId") ? node.get("modelId").asText() : "qwen3:14b";
        final var baseUrl = node.has("baseUrl") ? node.get("baseUrl").asText() : "http://127.0.0.1:11434";
        final var maxTokens = node.has("maxTokens") ? node.get("maxTokens").asInt() : 2048;
        return new AgentDefinition(id, role, triggers, toolIds, inputs, outputs, instructions, modelId, baseUrl, maxTokens);
    }

    private WorkflowDefinition toWorkflowDefinition(JsonNode node) {
        final var id = Id.of(node.get("id").asText());
        final var description = node.has("description") ? node.get("description").asText() : "";
        final var entryPoints = node.has("entryPoints")
            ? ofAll(node.get("entryPoints")).map(e -> Id.of(e.asText()))
            : List.<Id>empty();
        final var useAgentTriggers = node.has("useAgentTriggers")
            ? node.get("useAgentTriggers").asBoolean()
            : true;
        return new WorkflowDefinition(id, description, entryPoints, useAgentTriggers);
    }

    private List<AgentTrigger> parseTriggers(JsonNode node) {
        if (node == null || !node.isArray()) return List.empty();
        return ofAll(node).map(this::toAgentTrigger);
    }

    private AgentTrigger toAgentTrigger(JsonNode node) {
        final var targetAgentId = Id.of(node.get("targetAgentId").asText());
        final var condition = node.has("condition")
            ? AgentTrigger.TriggerCondition.valueOf(node.get("condition").asText().toUpperCase())
            : AgentTrigger.TriggerCondition.ON_OUTPUT;
        final var outputField = node.has("outputField") ? node.get("outputField").asText() : null;
        return new AgentTrigger(targetAgentId, condition, outputField);
    }

    private List<AgentData> parseAgentData(JsonNode node) {
        if (node == null || !node.isArray()) return List.empty();
        return ofAll(node).map(n -> new AgentData(
            n.has("name") ? n.get("name").asText() : "",
            n.has("type") ? AgentData.Type.valueOf(n.get("type").asText().toUpperCase()) : null));
    }

    private List<BddInstruction> parseInstructions(JsonNode node) {
        if (node == null || !node.isArray()) return List.empty();
        return ofAll(node).map(this::toBddInstruction);
    }

    private BddInstruction toBddInstruction(JsonNode node) {
        if (node.isTextual()) {
            String text = node.asText();
            if (text.startsWith("GIVEN:")) {
                return BddInstruction.given(text.substring(6).trim());
            } else if (text.startsWith("WHEN:")) {
                return BddInstruction.when(text.substring(5).trim());
            } else if (text.startsWith("THEN:")) {
                return BddInstruction.then(text.substring(5).trim());
            } else {
                return BddInstruction.then(text);
            }
        } else {
            String type = node.get("type").asText().toUpperCase();
            String text = node.get("text").asText();
            return switch (type) {
                case "GIVEN" -> BddInstruction.given(text);
                case "WHEN" -> BddInstruction.when(text);
                default -> BddInstruction.then(text);
            };
        }

    }

}