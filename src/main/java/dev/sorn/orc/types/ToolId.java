package dev.sorn.orc.types;

import jakarta.validation.ValidationException;

public record ToolId(String value) {

    public ToolId {
        if (value == null || value.isBlank()) {
            throw new ValidationException("ToolId cannot be blank");
        }
        if (!value.matches("[a-zA-Z0-9_-]+")) {
            throw new ValidationException("ToolId must match [a-zA-Z0-9_-]+");
        }
    }

    public static ToolId of(String value) {
        return new ToolId(value);
    }

}
