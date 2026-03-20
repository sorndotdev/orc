package dev.sorn.orc.tools;

import dev.sorn.orc.api.Tool;
import dev.sorn.orc.types.Result;
import dev.sorn.orc.types.ToolId;

import java.nio.file.Path;

import static dev.sorn.orc.types.Result.ok;
import static java.lang.System.getProperty;

public class PrintWorkingDirectoryTool implements Tool<Void, Path> {

    public static ToolId PRINT_WORKING_DIRECTORY_TOOL_ID = ToolId.of("print_working_directory");

    @Override
    public ToolId id() {
        return PRINT_WORKING_DIRECTORY_TOOL_ID;
    }

    @Override
    public Result<Path> execute(Void input) {
        return ok(Path.of(getProperty("user.dir")));
    }

    @Override
    public Class<Void> inputType() {
        return Void.class;
    }

}
