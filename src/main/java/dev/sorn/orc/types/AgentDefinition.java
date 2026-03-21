package dev.sorn.orc.types;

import io.vavr.collection.List;

public record AgentDefinition(
    Id id,
    AgentRole role,
    List<Id> toolIds,
    List<AgentData> inputs,
    List<AgentData> outputs,
    List<String> instructions
) {

    private AgentDefinition(Builder builder) {
        this(
            builder.id,
            builder.role,
            builder.toolIds,
            builder.inputs,
            builder.outputs,
            builder.instructions);
    }

    public static final class Builder {

        private Id id;
        private AgentRole role;
        private List<Id> toolIds;
        private List<AgentData> inputs;
        private List<AgentData> outputs;
        private List<String> instructions;

        public static Builder agentDefinition() {
            return new Builder();
        }

        public Builder id(Id id) {
            this.id = id;
            return this;
        }

        public Builder role(AgentRole role) {
            this.role = role;
            return this;
        }

        public Builder toolIds(List<Id> toolIds) {
            this.toolIds = toolIds;
            return this;
        }

        public Builder inputs(List<AgentData> inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder outputs(List<AgentData> outputs) {
            this.outputs = outputs;
            return this;
        }

        public Builder instructions(List<String> instructions) {
            this.instructions = instructions;
            return this;
        }

        public AgentDefinition build() {
            return new AgentDefinition(this);
        }

    }

}
