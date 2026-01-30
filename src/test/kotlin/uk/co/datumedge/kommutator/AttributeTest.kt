package uk.co.datumedge.kommutator

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class AttributeTest {
    @Test
    fun `string equality expression`() {
        val pk = Attribute.S("PK")

        val expression = pk eq "hello"

        assertSoftly(expression) {
            condition shouldBe "#PK = :PK"
            attributeNames shouldBe mapOf("#PK" to "PK")
            attributeValues shouldBe mapOf(":PK" to AttributeValue.S("hello"))
        }
    }

    @Test
    fun `number equality expression`() {
        val pk = Attribute.N("PK")

        val expression = pk eq 123

        expression.attributeValues shouldBe mapOf(":PK" to AttributeValue.N("123"))
    }

    @Test
    fun `bool equality expression`() {
        val pk = Attribute.Bool("PK")

        val expression = pk eq true

        expression.attributeValues shouldBe mapOf(":PK" to AttributeValue.Bool(true))
    }

    @Test
    fun `and operator with two string equalities`() {
        val pk = Attribute.S("PK")
        val sk = Attribute.S("SK")

        val expression = (pk eq "hello") and (sk eq "world")

        assertSoftly(expression) {
            condition shouldBe "#PK = :PK AND #SK = :SK"
            attributeNames shouldBe mapOf("#PK" to "PK", "#SK" to "SK")
            attributeValues shouldBe mapOf(":PK" to AttributeValue.S("hello"), ":SK" to AttributeValue.S("world"))
        }
    }
}




