package dev.sorn.orc.types

import dev.sorn.orc.OrcSpecification
import jakarta.validation.ValidationException

class ToolIdSpec extends OrcSpecification {

    def "creates valid ToolId or throws ValidationException"() {
        when:
        def result = ToolId.of(input)

        then:
        result.value() == expectedValue

        where:
        input               || expectedValue
        "validName123"      || "validName123"
        "another_Valid-Id"  || "another_Valid-Id"
    }

    def "throws ValidationException for invalid ToolId"() {
        when:
        ToolId.of(input)

        then:
        def ex = thrown(ValidationException)
        ex.message == expectedMessage

        where:
        input         || expectedMessage
        null          || "ToolId cannot be blank"
        ""            || "ToolId cannot be blank"
        "   "         || "ToolId cannot be blank"
        "invalid!@#"  || "ToolId must match [a-zA-Z0-9_-]+"
    }

}
