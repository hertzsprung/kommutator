package uk.co.datumedge.kommutator

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldMatchEach
import io.kotest.matchers.maps.haveValues
import io.kotest.matchers.maps.shouldMatchExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class AttributeTest {
    val string = Attribute.S("attr")
    val number = Attribute.N("attr")
    val bool = Attribute.Bool("attr")

    @Test
    fun `expression attribute value starts with colon attribute name`() {
        string.expressionValue shouldStartWith ":attr"
    }

    @Nested
    inner class Equality {
        @Test
        fun string() {
            val expression = string eq "hello"

            assertSoftly(expression) {
                attributeNames shouldBe mapOf("#attr" to "attr")
                attributeValues should haveValues(AttributeValue.S("hello"))
                condition shouldBe "#attr = ${attributeValues.keys.single()}"
            }
        }

        @Test
        fun number() {
            val expression = number eq 123

            expression.attributeValues should haveValues(AttributeValue.N("123"))
        }

        @Test
        fun boolean() {
            val expression = bool eq true

            expression.attributeValues should haveValues(AttributeValue.Bool(true))
        }
    }

    @Nested
    inner class LessThan {
        @Test
        fun number() {
            val expression = number lt 123

            assertSoftly(expression) {
                attributeValues should haveValues(AttributeValue.N("123"))
                condition shouldBe "#attr < ${attributeValues.keys.single()}"
            }
        }

        @Test
        fun string() {
            val expression = string lt "hello"

            expression.attributeValues shouldBe haveValues(AttributeValue.S("hello"))
        }
    }

    @Nested
    inner class LessThanOrEqual {
        @Test
        fun number() {
            val expression = number le 123

            assertSoftly(expression) {
                attributeValues should haveValues(AttributeValue.N("123"))
                condition shouldBe "#attr <= ${attributeValues.keys.single()}"
            }
        }

        @Test
        fun string() {
            val expression = string le "hello"

            expression.attributeValues should haveValues(AttributeValue.S("hello"))
        }
    }

    @Nested
    inner class GreaterThan {
        @Test
        fun number() {
            val expression = number gt 123

            assertSoftly(expression) {
                attributeValues should haveValues(AttributeValue.N("123"))
                condition shouldBe "#attr > ${attributeValues.keys.single()}"
            }
        }

        @Test
        fun string() {
            val expression = string gt "hello"

            expression.attributeValues shouldBe haveValues(AttributeValue.S("hello"))
        }
    }

    @Nested
    inner class GreaterThanOrEqual {
        @Test
        fun number() {
            val expression = number ge 123

            assertSoftly(expression) {
                attributeValues should haveValues(AttributeValue.N("123"))
                condition shouldBe "#attr >= ${attributeValues.keys.single()}"
            }
        }

        @Test
        fun string() {
            val expression = string ge "hello"

            expression.attributeValues should haveValues(AttributeValue.S("hello"))
        }
    }

    @Test
    fun `string begins with`() {
        val expression = string.beginsWith("hel")

        assertSoftly(expression) {
            attributeValues should haveValues(AttributeValue.S("hel"))
            condition shouldBe "begins_with(#attr, ${attributeValues.keys.single()})"
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
                attributeNames shouldBe mapOf("#PK" to "PK", "#SK" to "SK")

                val pkExpressionAttributeValue = attributeValues.keys.single { it.startsWith(":PK") }
                val skExpressionAttributeValue = attributeValues.keys.single { it.startsWith(":SK") }

                attributeValues shouldBe mapOf(
                    pkExpressionAttributeValue to AttributeValue.S("hello"),
                    skExpressionAttributeValue to AttributeValue.S("world")
                )
                condition shouldBe "#PK = $pkExpressionAttributeValue AND begins_with(#SK, $skExpressionAttributeValue)"
            }
        }

        @Test
        fun `two number inequalities`() {
            val pk = Attribute.N("PK")
            val sk = Attribute.N("SK")

            val expression = (pk le 123) and (sk lt 456)

            assertSoftly(expression) {
                val pkExpressionAttributeValue = attributeValues.keys.single { it.startsWith(":PK") }
                val skExpressionAttributeValue = attributeValues.keys.single { it.startsWith(":SK") }

                attributeValues shouldBe mapOf(
                    pkExpressionAttributeValue to AttributeValue.N("123"),
                    skExpressionAttributeValue to AttributeValue.N("456")
                )

                condition shouldBe "#PK <= $pkExpressionAttributeValue AND #SK < $skExpressionAttributeValue"
            }
        }

        @Test
        fun `two number inequalities on same attribute`() {
            val pk = Attribute.N("PK")

            val expression = (pk le 123) and (pk lt 456)

            assertSoftly(expression) {
                val leExpressionAttributeValue = attributeValues.filterValues { it == AttributeValue.N("123") }.keys.single()
                val ltExpressionAttributeValue = attributeValues.filterValues { it == AttributeValue.N("456") }.keys.single()

                attributeValues shouldBe mapOf(
                    leExpressionAttributeValue to AttributeValue.N("123"),
                    ltExpressionAttributeValue to AttributeValue.N("456")
                )
                condition shouldBe "#PK <= $leExpressionAttributeValue AND #PK < $ltExpressionAttributeValue"
            }
        }
    }
}




