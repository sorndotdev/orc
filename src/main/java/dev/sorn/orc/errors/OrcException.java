package dev.sorn.orc.errors;

import static java.lang.String.format;

public class OrcException extends RuntimeException {

    public OrcException(Throwable t) {
        super(t);
    }

    public OrcException(String message, Object... args) {
        super(format(message, args));
    }

}
