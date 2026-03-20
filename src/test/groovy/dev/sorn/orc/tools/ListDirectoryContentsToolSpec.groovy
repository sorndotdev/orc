package dev.sorn.orc.tools

import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Path

import static java.nio.file.Files.createFile

class ListDirectoryContentsToolSpec extends Specification {

    @TempDir
    Path tempDir

    def "returns directory contents list"() {
        given:
        def tool = new ListDirectoryContentsTool()

        and:
        createFile(tempDir.resolve("a.txt"))
        createFile(tempDir.resolve("b.txt"))

        when:
        def result = tool.execute(tempDir)

        then:
        result.isOk()
        with(result.get()) { r ->
            r.containsAll(["a.txt", "b.txt"])
            r.size() == 2
        }

        and:
        !result.isEmpty()
        !result.isError()
    }

    def "returns empty list for empty directory"() {
        given:
        def tool = new ListDirectoryContentsTool()

        when:
        def result = tool.execute(tempDir)

        then:
        result.isEmpty()

        and:
        !result.isOk()
        !result.isError()
    }

    def "returns error for non existing directory"() {
        given:
        def tool = new ListDirectoryContentsTool()
        def missing = tempDir.resolve("some_invalid_path")

        when:
        def result = tool.execute(missing)

        then:
        result.isError()
        with(result.getError()) { err ->
            err.getMessage() == "dev.sorn.orc.errors.ToolError: '${tempDir.toString()}/some_invalid_path' directory not found"
        }

        and:
        !result.isOk()
        !result.isEmpty()
    }

    def "returns error when path is not a directory"() {
        given:
        def tool = new ListDirectoryContentsTool()
        def file = createFile(tempDir.resolve("not_a_dir.txt"))

        when:
        def result = tool.execute(file)

        then:
        result.isError()
        with(result.getError()) { err ->
            err.getMessage() == "dev.sorn.orc.errors.ToolError: '${file.toString()}' is not a directory"
        }

        and:
        !result.isOk()
        !result.isEmpty()
    }

}
