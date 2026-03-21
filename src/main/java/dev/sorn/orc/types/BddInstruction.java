package dev.sorn.orc.types;

import static dev.sorn.orc.types.BddInstruction.Type.GIVEN;
import static dev.sorn.orc.types.BddInstruction.Type.THEN;
import static dev.sorn.orc.types.BddInstruction.Type.WHEN;

public record BddInstruction(Type type, String text) {

    public enum Type {
        GIVEN, WHEN, THEN
    }

    public static BddInstruction given(String text) {
        return new BddInstruction(GIVEN, text);
    }

    public static BddInstruction when(String text) {
        return new BddInstruction(WHEN, text);
    }

    public static BddInstruction then(String text) {
        return new BddInstruction(THEN, text);
    }

}
