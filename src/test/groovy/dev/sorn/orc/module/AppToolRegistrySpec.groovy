package dev.sorn.orc.module

import dev.sorn.orc.OrcSpecification
import dev.sorn.orc.errors.OrcException
import dev.sorn.orc.tools.ListDirectoryContentsTool
import dev.sorn.orc.tools.PrintWorkingDirectoryTool
import dev.sorn.orc.types.Id

class AppToolRegistrySpec extends OrcSpecification {

    def "registers and retrieves tools"() {
        given:
        def registry = new AppToolRegistry()
        def pwdTool = new PrintWorkingDirectoryTool()
        def listTool = new ListDirectoryContentsTool()

        when:
        registry.register(pwdTool)
        registry.register(listTool)

        then:
        registry.get(Id.of("print_working_directory_tool")) == pwdTool
        registry.get(Id.of("list_directory_contents_tool")) == listTool
    }

    def "throws error when registering existing tool"() {
        given:
        def registry = new AppToolRegistry()
        def pwdTool = new PrintWorkingDirectoryTool()

        when:
        registry.register(pwdTool)
        registry.register(pwdTool)

        then:
        def ex = thrown(OrcException)
        ex.message == "'print_working_directory_tool' tool is already registered"
    }

    def "throws error when retrieving unregistered tool"() {
        given:
        def registry = new AppToolRegistry()

        when:
        registry.get(Id.of("non_existent_tool"))

        then:
        def ex = thrown(OrcException)
        ex.message == "'non_existent_tool' tool is not registered"
    }

}