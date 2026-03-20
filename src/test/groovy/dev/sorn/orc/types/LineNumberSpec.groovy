package dev.sorn.orc.types

import dev.sorn.orc.OrcSpecification
import jakarta.validation.ValidationException

class LineNumberSpec extends OrcSpecification {

    def "creates Line with valid value"() {
        given:
        def value = 42

        when:
        def line = LineNumber.of(value)

        then:
        line.value() == 42
    }

    def "throws ValidationException for invalid values"() {
        when:
        LineNumber.of(input)

        then:
        def ex = thrown(ValidationException)
        ex.message == expectedMessage

        where:
        input || expectedMessage
        0     || "line number must be >= 1, got 0"
        -1    || "line number must be >= 1, got -1"
        -100  || "line number must be >= 1, got -100"
    }

}
