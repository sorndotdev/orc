package dev.sorn.orc

import dev.sorn.orc.api.ToolRegistry
import dev.sorn.orc.module.AppToolRegistry
import dev.sorn.orc.tools.ListDirectoryContentsTool
import dev.sorn.orc.tools.PrintWorkingDirectoryTool
import spock.lang.Specification

class OrcSpecification extends Specification {

    static ToolRegistry toolRegistry = new AppToolRegistry()

    static {
        toolRegistry.register(new ListDirectoryContentsTool())
        toolRegistry.register(new PrintWorkingDirectoryTool())
    }

}
