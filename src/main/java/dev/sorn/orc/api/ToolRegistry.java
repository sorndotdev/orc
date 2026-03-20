package dev.sorn.orc.api;

import dev.sorn.orc.types.ToolId;

public interface ToolRegistry {

    Tool<?, ?> get(ToolId id);

    <I, O> void register(Tool<I, O> tool);

}
