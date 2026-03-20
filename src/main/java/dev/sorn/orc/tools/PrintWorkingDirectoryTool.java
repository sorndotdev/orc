package dev.sorn.orc.tools;

import dev.sorn.orc.api.Tool;
import dev.sorn.orc.types.Result;
import dev.sorn.orc.types.Id;

import java.nio.file.Path;

import static dev.sorn.orc.types.Result.ok;
import static java.lang.System.getProperty;

public class PrintWorkingDirectoryTool implements Tool<Void, Path> {

    public static Id PRINT_WORKING_DIRECTORY_TOOL_ID = Id.of("print_working_directory_tool");

    @Override
    public Id id() {
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
