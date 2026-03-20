package dev.sorn.orc.types

import dev.sorn.orc.OrcSpecification
import jakarta.validation.ValidationException

class IdSpec extends OrcSpecification {

    def "creates valid id"() {
        when:
        def result = Id.of(input)

        then:
        result.value() == expectedValue

        where:
        input          || expectedValue
        "validId123"   || "validId123"
        "valid_Id-123" || "valid_Id-123"
    }

    def "throws for invalid id"() {
        when:
        Id.of(input)

        then:
        def ex = thrown(ValidationException)
        ex.message == expectedMessage

        where:
        input        || expectedMessage
        null         || "Id cannot be blank"
        ""           || "Id cannot be blank"
        "   "        || "Id cannot be blank"
        "invalid!@#" || "Id must match [a-zA-Z0-9_-]+"
    }

}
