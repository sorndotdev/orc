package dev.sorn.orc.tools

import dev.sorn.orc.OrcSpecification
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path

import static dev.sorn.orc.tools.GrepTool.GREP_TOOL_ID

class GrepToolSpec extends OrcSpecification {

    @TempDir
    Path tempDir

    def "finds pattern in single file"() {
        given:
        def file = tempDir.resolve("test.txt")
        Files.writeString(file, "line1\nTODO: fix\nline3")
        def tool = toolRegistry.get(GREP_TOOL_ID)

        when:
        def result = tool.execute(new GrepTool.Input("TODO", file, true, true))

        then:
        result.fold(
            matches -> {
                matches.size() == 1
                matches[0].file() == file
                matches[0].lineNumber().value() == 2
            },
            { false }
        )
    }

    def "recursively searches directory"() {
        given:
        def subDir = tempDir.resolve("sub")
        Files.createDirectory(subDir)
        def file1 = tempDir.resolve("a.txt")
        def file2 = subDir.resolve("b.txt")
        Files.writeString(file1, "no match")
        Files.writeString(file2, "match here")
        def tool = toolRegistry.get(GREP_TOOL_ID)

        when:
        def result = tool.execute(new GrepTool.Input("match", tempDir, true, true))

        then:
        result.fold(
            matches -> {
                matches.size() == 1
                matches[0].file() == file2
                matches[0].lineNumber().value() == 1
            },
            { false }
        )
    }

    def "case insensitive search"() {
        given:
        def file = tempDir.resolve("test.txt")
        Files.writeString(file, "Hello World")
        def tool = toolRegistry.get(GREP_TOOL_ID)

        when:
        def result = tool.execute(new GrepTool.Input("hello", file, true, false))

        then:
        result.fold(
            matches -> matches.size() == 1,
            { false }
        )
    }
}