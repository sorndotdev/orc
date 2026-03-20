package dev.sorn.orc.types;

import dev.sorn.orc.errors.ToolError;
import io.vavr.control.Option;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;

public record Result<T>(Option<T> value, Option<ToolError> error) {

    public static <T> Result<T> ok(T value) {
        return new Result<>(some(value), none());
    }

    public static <T> Result<T> empty() {
        return new Result<>(none(), none());
    }

    public static <T> Result<T> error(ToolError error) {
        return new Result<>(none(), some(error));
    }

    public T get() {
        return value.get();
    }

    public ToolError getError() {
        return error.get();
    }

    public boolean isOk() {
        return !value.isEmpty() && error.isEmpty();
    }

    public boolean isEmpty() {
        return value.isEmpty() && error.isEmpty();
    }

    public boolean isError() {
        return value.isEmpty() && !error.isEmpty();
    }

}
