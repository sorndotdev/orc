package dev.sorn.orc.tools

import dev.sorn.orc.OrcSpecification

import static dev.sorn.orc.tools.PrintWorkingDirectoryTool.PRINT_WORKING_DIRECTORY_TOOL_ID

class PrintWorkingDirectoryToolSpec extends OrcSpecification {

    def "returns current working directory"() {
        given:
        def tool = toolRegistry.get(PRINT_WORKING_DIRECTORY_TOOL_ID)

        when:
        def result = tool.execute(null)

        then:
        result.fold(value -> {
            value.toString() == System.getProperty("user.dir")
        }, {}, {})
    }

}
