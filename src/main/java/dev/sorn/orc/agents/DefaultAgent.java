package dev.sorn.orc.agents;

import dev.sorn.orc.types.AgentDefinition;
import dev.sorn.orc.api.LlmClient;
import dev.sorn.orc.api.Result;
import dev.sorn.orc.api.ToolRegistry;

import java.util.StringJoiner;

public final class DefaultAgent extends BaseAgent {

    private DefaultAgent(Builder builder) {
        super(builder.agentDefinition, builder.toolRegistry, builder.llmClient);
    }

    @Override
    public Result<?> complete(String prompt) {
        final var promptJoiner = new StringJoiner("\n");
        promptJoiner.add("## Instructions");
        agentDefinition.instructions().forEach(promptJoiner::add);
        promptJoiner.add("");
        promptJoiner.add("## Prompt");
        promptJoiner.add(prompt);
        promptJoiner.add("");
        return llmClient.complete(promptJoiner.toString());
    }

    public static final class Builder {

        private AgentDefinition agentDefinition;
        private ToolRegistry toolRegistry;
        private LlmClient llmClient;

        private Builder() {}

        public static Builder defaultAgent() {
            return new Builder();
        }

        public Builder agentDefinition(AgentDefinition agentDefinition) {
            this.agentDefinition = agentDefinition;
            return this;
        }

        public Builder toolRegistry(ToolRegistry toolRegistry) {
            this.toolRegistry = toolRegistry;
            return this;
        }

        public Builder llmClient(LlmClient llmClient) {
            this.llmClient = llmClient;
            return this;
        }

        public DefaultAgent build() {
            return new DefaultAgent(this);
        }

    }

}
