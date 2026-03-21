package dev.sorn.orc.tools

import dev.sorn.orc.OrcSpecification
import dev.sorn.orc.errors.OrcException
import spock.lang.TempDir

import java.nio.file.Path

import static dev.sorn.orc.tools.ListDirectoryContentsTool.LIST_DIRECTORY_CONTENTS_TOOL_ID
import static java.nio.file.Files.createFile

class ListDirectoryContentsToolSpec extends OrcSpecification {

    @TempDir
    Path tempDir

    def "returns directory contents list"() {
        given:
        def tool = toolRegistry.get(LIST_DIRECTORY_CONTENTS_TOOL_ID)

        and:
        createFile(tempDir.resolve("a.txt"))
        createFile(tempDir.resolve("b.txt"))

        when:
        def result = tool.execute(tempDir)

        then:
        result.fold(
            value -> {
                assert value.containsAll(["a.txt", "b.txt"])
                value.size() == 2
            },
            { false })
    }

    def "returns empty list for empty directory"() {
        given:
        def tool = toolRegistry.get(LIST_DIRECTORY_CONTENTS_TOOL_ID)

        when:
        def result = tool.execute(tempDir)

        then:
        result.fold(
            value -> value.isEmpty(),
            { false })
    }

    def "returns error for non existing directory"() {
        given:
        def tool = toolRegistry.get(LIST_DIRECTORY_CONTENTS_TOOL_ID)
        def missing = tempDir.resolve("some_invalid_path")

        when:
        def result = tool.execute(missing)

        then:
        result.fold(
            { false },
            err -> {
                err instanceof OrcException
                err.getMessage() == "dev.sorn.orc.errors.OrcException: '${tempDir.toString()}/some_invalid_path' directory not found"
            })
    }

    def "returns error when path is not a directory"() {
        given:
        def tool = toolRegistry.get(LIST_DIRECTORY_CONTENTS_TOOL_ID)
        def file = createFile(tempDir.resolve("not_a_dir.txt"))

        when:
        def result = tool.execute(file)

        then:
        result.fold(
            { false },
            err -> {
                err instanceof OrcException
                err.getMessage() == "dev.sorn.orc.errors.OrcException: '${file.toString()}' is not a directory"
            })
    }

}
