package uk.co.datumedge.kommutator

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class AttributeTest {
    @Nested
    inner class Equality {
        @Test
        fun string() {
            val pk = Attribute.S("PK")

            val expression = pk eq "hello"

            assertSoftly(expression) {
                condition shouldBe "#PK = :PK"
                attributeNames shouldBe mapOf("#PK" to "PK")
                attributeValues shouldBe mapOf(":PK" to AttributeValue.S("hello"))
            }
        }

        @Test
        fun number() {
            val pk = Attribute.N("PK")

            val expression = pk eq 123

            expression.attributeValues shouldBe mapOf(":PK" to AttributeValue.N("123"))
        }

        @Test
        fun boolean() {
            val pk = Attribute.Bool("PK")

            val expression = pk eq true

            expression.attributeValues shouldBe mapOf(":PK" to AttributeValue.Bool(true))
        }
    }

    @Nested
    inner class LessThan {
        @Test
        fun number() {
            val pk = Attribute.N("PK")

            val expression = pk lt 123

            assertSoftly(expression) {
                condition shouldBe "#PK < :PK"
                attributeNames shouldBe mapOf("#PK" to "PK")
                attributeValues shouldBe mapOf(":PK" to AttributeValue.N("123"))
            }
        }

        @Test
        fun string() {
            val pk = Attribute.S("PK")

            val expression = pk lt "hello"

            expression.attributeValues shouldBe mapOf(":PK" to AttributeValue.S("hello"))
        }
    }

    @Nested
    inner class LessThanOrEqual {
        @Test
        fun number() {
            val pk = Attribute.N("PK")

            val expression = pk le 123

            assertSoftly(expression) {
                condition shouldBe "#PK <= :PK"
                attributeNames shouldBe mapOf("#PK" to "PK")
                attributeValues shouldBe mapOf(":PK" to AttributeValue.N("123"))
            }
        }

        @Test
        fun string() {
            val pk = Attribute.S("PK")

            val expression = pk le "hello"

            expression.attributeValues shouldBe mapOf(":PK" to AttributeValue.S("hello"))
        }
    }

    @Nested
    inner class GreaterThan {
        @Test
        fun number() {
            val pk = Attribute.N("PK")

            val expression = pk gt 123

            assertSoftly(expression) {
                condition shouldBe "#PK > :PK"
                attributeNames shouldBe mapOf("#PK" to "PK")
                attributeValues shouldBe mapOf(":PK" to AttributeValue.N("123"))
            }
        }

        @Test
        fun string() {
            val pk = Attribute.S("PK")

            val expression = pk gt "hello"

            expression.attributeValues shouldBe mapOf(":PK" to AttributeValue.S("hello"))
        }
    }

    @Nested
    inner class GreaterThanOrEqual {
        @Test
        fun number() {
            val pk = Attribute.N("PK")

            val expression = pk ge 123

            assertSoftly(expression) {
                condition shouldBe "#PK >= :PK"
                attributeNames shouldBe mapOf("#PK" to "PK")
                attributeValues shouldBe mapOf(":PK" to AttributeValue.N("123"))
            }
        }

        @Test
        fun string() {
            val pk = Attribute.S("PK")

            val expression = pk ge "hello"

            expression.attributeValues shouldBe mapOf(":PK" to AttributeValue.S("hello"))
        }
    }

    @Nested
    inner class And {
        @Test
        fun `two string equalities`() {
            val pk = Attribute.S("PK")
            val sk = Attribute.S("SK")

            val expression = (pk eq "hello") and (sk eq "world")

            assertSoftly(expression) {
                condition shouldBe "#PK = :PK AND #SK = :SK"
                attributeNames shouldBe mapOf("#PK" to "PK", "#SK" to "SK")
                attributeValues shouldBe mapOf(":PK" to AttributeValue.S("hello"), ":SK" to AttributeValue.S("world"))
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
                attributeValues shouldBe mapOf(":PK" to AttributeValue.N("123"), ":SK" to AttributeValue.N("456"))
            }
        }
    }
}




