package dev.sorn.orc.api;

import dev.sorn.orc.types.Id;

public interface ToolRegistry {

    <I, O> Tool<I, O> get(Id id);

    <I, O> void register(Tool<I, O> tool);

}
