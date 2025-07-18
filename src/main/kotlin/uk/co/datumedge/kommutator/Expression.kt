package uk.co.datumedge.kommutator

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue

interface Expression {
    val condition: String
    val attributeNames: Map<String, String>
    val attributeValues: Map<String, AttributeValue>
}

class Eq(private val attribute: Attribute, private val value: AttributeValue): Expression {
    override val condition: String get() = "${attribute.expressionName} = ${attribute.expressionValue}"
    override val attributeNames: Map<String, String> get() = mapOf(attribute.expressionName to attribute.name)
    override val attributeValues: Map<String, AttributeValue> get() = mapOf(attribute.expressionValue to value)

    infix fun and(right: Expression): Expression = And(this, right)
}

internal class And(private val left: Expression, private val right: Expression) : Expression {
    override val condition: String get() = "${left.condition} AND ${right.condition}"
    override val attributeNames: Map<String, String> get() = left.attributeNames + right.attributeNames
    override val attributeValues: Map<String, AttributeValue> get() = left.attributeValues + right.attributeValues
}