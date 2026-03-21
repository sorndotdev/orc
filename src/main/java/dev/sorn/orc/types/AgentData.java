package dev.sorn.orc.types;

import io.vavr.collection.List;

public record AgentData(
    String name,
    Type type
) {

    private AgentData(Builder builder) {
        this(builder.name, builder.type);
    }

    public enum Type {
        BOOLEAN(Boolean.class),
        COLLECTION(List.class),
        STRING(String.class);

        private final Class<?> javaClass;

        Type(Class<?> javaClass) {
            this.javaClass = javaClass;
        }

        public Class<?> javaClass() {
            return javaClass;
        }
    }

    public static final class Builder {

        private String name;
        private Type type;

        private Builder() {
        }

        public static Builder agentData() {
            return new Builder();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public AgentData build() {
            return new AgentData(this);
        }

    }

}
