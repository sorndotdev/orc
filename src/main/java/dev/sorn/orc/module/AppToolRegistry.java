package dev.sorn.orc.module;

import dev.sorn.orc.api.Tool;
import dev.sorn.orc.api.ToolRegistry;
import dev.sorn.orc.errors.ToolError;
import dev.sorn.orc.types.ToolId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppToolRegistry implements ToolRegistry {

    private final Map<ToolId, Tool<?, ?>> tools = new ConcurrentHashMap<>();

    @Override
    public Tool<?, ?> get(ToolId id) {
        final var tool = tools.get(id);
        if (tool == null) {
            throw new ToolError("'%s' tool is not registered", id.value());
        }
        return tool;
    }

    @Override
    public <I, O> void register(Tool<I, O> tool) {
        tools.put(tool.id(), tool);
    }

    public int size() {
        return tools.size();
    }

}
