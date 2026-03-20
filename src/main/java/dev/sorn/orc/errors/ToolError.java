package dev.sorn.orc.errors;

import static java.lang.String.format;

public class ToolError extends RuntimeException {

    public ToolError(Throwable t) {
        super(t);
    }

    public ToolError(String message, Object... args) {
        super(format(message, args));
    }

}
