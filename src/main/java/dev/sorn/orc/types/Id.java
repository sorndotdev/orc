package dev.sorn.orc.types;

import jakarta.validation.ValidationException;

public record Id(String value) {

    public Id {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Id cannot be blank");
        }
        if (!value.matches("[a-zA-Z0-9_-]+")) {
            throw new ValidationException("Id must match [a-zA-Z0-9_-]+");
        }
    }

    public static Id of(String value) {
        return new Id(value);
    }

}
