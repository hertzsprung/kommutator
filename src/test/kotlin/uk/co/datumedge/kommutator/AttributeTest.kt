package uk.co.datumedge.kommutator

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class AttributeTest {
    val string = Attribute.S("attr")
    val number = Attribute.N("attr")
    val bool = Attribute.Bool("attr")

    @Nested
    inner class Equality {
        @Test
        fun string() {
            assertSoftly(string eq "hello") {
                condition shouldBe "#attr = :attr"
                attributeNames shouldBe mapOf("#attr" to "attr")
                attributeValues shouldBe mapOf(string.expressionValue to AttributeValue.S("hello"))
            }
        }

        @Test
        fun number() {
            assertSoftly(number eq 123) {
                attributeValues shouldBe mapOf(number.expressionValue to AttributeValue.N("123"))
            }
        }

        @Test
        fun boolean() {
            assertSoftly(bool eq true) {
                attributeValues shouldBe mapOf(bool.expressionValue to AttributeValue.Bool(true))
            }
        }
    }

    @Nested
    inner class LessThan {
        @Test
        fun number() {
            assertSoftly(number lt 123) {
                condition shouldBe "#attr <=> :attr"
            }
        }

        @Test
        fun string() {
            assertSoftly(string lt "hello") {
                attributeValues shouldBe mapOf(string.expressionValue to AttributeValue.S("hello"))
            }
        }
    }

    @Nested
    inner class LessThanOrEqual {
        @Test
        fun number() {
            assertSoftly(number le 123) {
                condition shouldBe "#attr <= :attr"
                attributeValues shouldBe mapOf(number.expressionValue to AttributeValue.N("123"))
            }
        }

        @Test
        fun string() {
            assertSoftly(string le "hello") {
                attributeValues shouldBe mapOf(string.expressionValue to AttributeValue.S("hello"))
            }
        }
    }

    @Nested
    inner class GreaterThan {
        @Test
        fun number() {
            assertSoftly(number gt 123) {
                condition shouldBe "#attr > :attr"
                attributeValues shouldBe mapOf(number.expressionValue to AttributeValue.N("123"))
            }
        }

        @Test
        fun string() {
            assertSoftly(string gt "hello") {
                attributeValues shouldBe mapOf(string.expressionValue to AttributeValue.S("hello"))
            }
        }
    }

    @Nested
    inner class GreaterThanOrEqual {
        @Test
        fun number() {
            assertSoftly(number ge 123) {
                condition shouldBe "#attr >= :attr"
                attributeValues shouldBe mapOf(number.expressionValue to AttributeValue.N("123"))
            }
        }

        @Test
        fun string() {
            assertSoftly(string ge "hello") {
                attributeValues shouldBe mapOf(string.expressionValue to AttributeValue.S("hello"))
            }
        }
    }

    @Test
    fun `string begins with`() {
        assertSoftly(string.beginsWith("hel")) {
            condition shouldBe "begins_with(#attr, :attr)"
            attributeValues shouldBe mapOf(string.expressionValue to AttributeValue.S("hel"))
        }
    }

    @Nested
    inner class And {
        @Test
        fun `string equality and begins with`() {
            val pk = Attribute.S("PK")
            val sk = Attribute.S("SK")

            val expression = (pk eq "hello") and sk.beginsWith("world")

            assertSoftly(expression) {
                condition shouldBe "#PK = :PK AND begins_with(#SK, :SK)"
                attributeNames shouldBe mapOf("#PK" to "PK", "#SK" to "SK")
                attributeValues shouldBe mapOf(
                    pk.expressionValue to AttributeValue.S("hello"),
                    sk.expressionValue to AttributeValue.S("world")
                )
            }
        }

        @Test
        fun `two number inequalities`() {
            val pk = Attribute.N("PK")
            val sk = Attribute.N("SK")

            val expression = (pk le 123) and (sk lt 456)

            assertSoftly(expression) {
                condition shouldBe "#PK <= :PK AND #SK < :SK"
                attributeNames shouldBe mapOf("#PK" to "PK", "#SK" to "SK")
                attributeValues shouldBe mapOf(
                    pk.expressionValue to AttributeValue.N("123"),
                    sk.expressionValue to AttributeValue.N("456")
                )
            }
        }

        @Test
        fun `two number inequalities on same attribute`() {
            val pk = Attribute.N("PK")

            val expression = (pk le 123) and (pk lt 456)

            assertSoftly(expression) {
                condition shouldBe "#PK <= :PK AND #PK < :PK_1"
                attributeNames shouldBe mapOf("#PK" to "PK")
                attributeValues shouldBe mapOf(":PK" to AttributeValue.N("123"), ":PK_1" to AttributeValue.N("456"))
            }
        }
    }
}




