package dev.sorn.orc.types;

public record AgentTrigger(
    Id targetAgentId,
    TriggerCondition condition,
    String outputField
) {

    public enum TriggerCondition {
        ON_OUTPUT,
        ON_SUCCESS,
        ON_FAILURE,
        ALWAYS
    }

    private AgentTrigger(Builder builder) {
        this(
            builder.targetAgentId,
            builder.condition,
            builder.outputField
        );
    }

    public static final class Builder {
        private Id targetAgentId;
        private TriggerCondition condition = TriggerCondition.ON_OUTPUT;
        private String outputField;

        private Builder() {}

        public static Builder agentTrigger() {
            return new Builder();
        }

        public Builder targetAgentId(Id targetAgentId) {
            this.targetAgentId = targetAgentId;
            return this;
        }

        public Builder condition(TriggerCondition condition) {
            this.condition = condition;
            return this;
        }

        public Builder outputField(String outputField) {
            this.outputField = outputField;
            return this;
        }

        public AgentTrigger build() {
            return new AgentTrigger(this);
        }
    }

}
