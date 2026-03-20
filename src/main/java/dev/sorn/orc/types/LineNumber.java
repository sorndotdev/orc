package dev.sorn.orc.types;

import jakarta.validation.ValidationException;

public record LineNumber(int value) {

    public LineNumber {
        if (value < 1) {
            throw new ValidationException("line number must be >= 1, got " + value);
        }
    }

    public static LineNumber of(int value) {
        return new LineNumber(value);
    }

}
