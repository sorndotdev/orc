package dev.sorn.orc.types

import dev.sorn.orc.OrcSpecification
import jakarta.validation.ValidationException

import static io.vavr.control.Option.none
import static io.vavr.control.Option.some

class LineNumberRangeSpec extends OrcSpecification {

    def "creates empty line number range"() {
        when:
        def range = LineNumberRange.empty()

        then:
        range.from() == none()
        range.to() == none()
    }

    def "creates range from specific line only"() {
        given:
        def from = LineNumber.of(28)

        when:
        def range = LineNumberRange.from(from)

        then:
        range.from() == some(from)
        range.to() == none()
    }

    def "creates range to specific line only"() {
        given:
        def to = LineNumber.of(42)

        when:
        def range = LineNumberRange.to(to)

        then:
        range.from() == none()
        range.to() == some(to)
    }

    def "creates range with both from and to"() {
        given:
        def from = LineNumber.of(28)
        def to = LineNumber.of(42)

        when:
        def range = LineNumberRange.of(from, to)

        then:
        range.from() == some(from)
        range.to() == some(to)
    }

    def "throws exception if from ≥ to"() {
        given:
        def from = LineNumber.of(fromValue)
        def to = LineNumber.of(toValue)

        when:
        new LineNumberRange(some(from), some(to))

        then:
        def ex = thrown(ValidationException)
        ex.message == "invalid line number range: ${fromValue}, ${toValue}"

        where:
        fromValue | toValue
        13      | 5
        13      | 13
    }

}
