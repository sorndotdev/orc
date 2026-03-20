package dev.sorn.orc.api;

import dev.sorn.orc.types.Id;

public interface ToolRegistry {

    Tool<?, ?> get(Id id);

    <I, O> void register(Tool<I, O> tool);

}
