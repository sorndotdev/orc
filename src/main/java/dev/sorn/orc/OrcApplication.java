package dev.sorn.orc;

import dev.sorn.orc.agents.DefaultAgent;
import dev.sorn.orc.clients.DefaultJsonHttpClient;
import dev.sorn.orc.clients.OllamaClient;
import dev.sorn.orc.module.AgentFactory;
import dev.sorn.orc.module.AppToolRegistry;
import dev.sorn.orc.tools.FileReaderTool;
import dev.sorn.orc.tools.FileWriterTool;
import dev.sorn.orc.tools.GrepTool;
import dev.sorn.orc.tools.ListDirectoryContentsTool;
import dev.sorn.orc.tools.PrintWorkingDirectoryTool;
import dev.sorn.orc.types.AgentTrigger;
import dev.sorn.orc.types.Id;
import dev.sorn.orc.types.WorkflowDefinition;
import io.vavr.collection.HashSet;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import static dev.sorn.orc.agents.DefaultAgent.Builder.defaultAgent;
import static java.nio.file.Files.readString;

public class OrcApplication {
    public static void main(String[] args) throws IOException {
        String prompt;
        if (args.length > 0) {
            prompt = String.join(" ", args);
        } else {
            System.out.print("Enter your prompt: ");
            try (Scanner scanner = new Scanner(System.in)) {
                prompt = scanner.nextLine();
            }
        }

        final var jsonPath = Path.of("src/main/resources/agents.def.json");
        final var json = readString(jsonPath);
        final var jsonHttpClient = new DefaultJsonHttpClient();
        final var registry = new AppToolRegistry();
        registry.register(new FileReaderTool(Files::newBufferedReader));
        registry.register(new ListDirectoryContentsTool());
        registry.register(new PrintWorkingDirectoryTool());
        registry.register(new GrepTool());
        registry.register(new FileWriterTool());

        final var agentFactory = new AgentFactory();
        final var agentDefinitions = agentFactory.loadFromJson(json);
        final var workflowDefinitions = agentFactory.loadWorkflowsFromJson(json);

        final var agentMap = new HashMap<Id, DefaultAgent>();
        agentDefinitions.forEach(def -> {
            var llmClient = new OllamaClient(
                Id.of(def.modelId()),
                jsonHttpClient,
                URI.create(def.baseUrl()),
                def.maxTokens()
            );
            var agent = defaultAgent()
                .agentDefinition(def)
                .toolRegistry(registry)
                .llmClient(llmClient)
                .progressConsumer(createProgressConsumer())
                .build();
            agentMap.put(def.id(), agent);
        });

        if (!workflowDefinitions.isEmpty()) {
            workflowDefinitions.forEach(workflow -> {
                System.out.println("\n=== Workflow: " + workflow.id().value() + " ===");
                System.out.println("Description: " + workflow.description());
                executeGraphWorkflow(workflow, agentMap, prompt);
            });
        } else {
            agentMap.values().forEach(agent -> {
                System.out.println("\nAgent: " + agent.id().value());
                System.out.println("Role: " + agent.role());
                System.out.println("--- Processing ---");
                var result = agent.complete(prompt);
                printResult(result);
            });
        }
    }

    private static void executeGraphWorkflow(
        WorkflowDefinition workflow,
        Map<Id, DefaultAgent> agentMap,
        String initialPrompt
    ) {
        var visited = HashSet.<Id>empty();
        var queue = new LinkedList<Id>();
        var agentOutputs = new HashMap<Id, String>();
        var currentPrompt = initialPrompt;

        workflow.entryPoints().forEach(queue::offer);

        while (!queue.isEmpty()) {
            var agentId = queue.poll();

            if (visited.contains(agentId)) {
                continue;
            }

            var agent = agentMap.get(agentId);
            if (agent == null) {
                System.err.println("Agent not found: " + agentId.value());
                continue;
            }

            visited = visited.add(agentId);
            System.out.println("\n--- Executing: " + agentId.value() + " ---");

            var result = agent.complete(currentPrompt);
            var output = printResult(result);
            agentOutputs.put(agentId, output);

            final var finalVisited = visited;
            agent.triggers().forEach(trigger -> {
                if (shouldTrigger(trigger, output)) {
                    if (!finalVisited.contains(trigger.targetAgentId())) {
                        queue.offer(trigger.targetAgentId());
                        System.out.println("  -> Triggered: " + trigger.targetAgentId().value());
                    }
                }
            });

            currentPrompt = initialPrompt + "\n\nPrevious context:\n" + output;
        }
    }

    private static boolean shouldTrigger(AgentTrigger trigger, String output) {
        return switch (trigger.condition()) {
            case ALWAYS -> true;
            case ON_SUCCESS -> !output.contains("Error:") && !output.contains("Failure");
            case ON_FAILURE -> output.contains("Error:") || output.contains("Failure");
            case ON_OUTPUT -> trigger.outputField() == null || output.contains(trigger.outputField());
        };
    }

    private static String printResult(dev.sorn.orc.api.Result<String> result) {
        var output = new StringBuilder();
        result.fold(
            val -> {
                if (val == null || val.isBlank()) {
                    output.append("[Empty response]");
                } else {
                    output.append("\n--- Result ---\n").append(val);
                }
                return val;
            },
            err -> {
                System.err.println("\nError: " + err.getMessage());
                return err.getMessage();
            }
        );
        System.out.println(output);
        return output.toString();
    }

    private static Consumer<String> createProgressConsumer() {
        final var spinner = new char[]{'⠋', '⠙', '⠹', '⠸', '⠼', '⠴', '⠦', '⠧'};
        final var idx = new AtomicInteger(0);
        return message -> {
            System.out.print("\r\033[2K");
            var frame = spinner[idx.getAndIncrement() % spinner.length];
            System.out.print("[" + frame + "] " + message);
            System.out.flush();
        };
    }

}
