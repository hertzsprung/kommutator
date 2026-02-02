package uk.co.datumedge.kommutator

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue
import java.util.UUID

interface Expression {
    val condition: String
    val attributeNames: Map<String, String>
    val attributeValues: Map<String, AttributeValue>
}

abstract class RelationalOperator(private val attribute: Attribute, private val value: AttributeValue): Expression {
    protected val expressionValue = "${attribute.expressionValue}_${randomSuffix()}"
    override val attributeNames: Map<String, String> get() = mapOf(attribute.expressionName to attribute.name)
    override val attributeValues: Map<String, AttributeValue> get() = mapOf(expressionValue to value)

    infix fun and(right: Expression): Expression = And(this, right)

    private fun randomSuffix(): String = UUID.randomUUID().toString().split("-", limit = 2)[0]
}

class Eq(private val attribute: Attribute, value: AttributeValue): RelationalOperator(attribute, value) {
    override val condition: String get() = "${attribute.expressionName} = $expressionValue"
}

class Lt(private val attribute: Attribute, value: AttributeValue): RelationalOperator(attribute, value) {
    override val condition: String get() = "${attribute.expressionName} < $expressionValue"
}

class Le(private val attribute: Attribute, value: AttributeValue): RelationalOperator(attribute, value) {
    override val condition: String get() = "${attribute.expressionName} <= $expressionValue"
}

class Gt(private val attribute: Attribute, value: AttributeValue): RelationalOperator(attribute, value) {
    override val condition: String get() = "${attribute.expressionName} > $expressionValue"
}

class Ge(private val attribute: Attribute, value: AttributeValue): RelationalOperator(attribute, value) {
    override val condition: String get() = "${attribute.expressionName} >= $expressionValue"
}

internal class BeginsWith(private val attribute: Attribute, substr: AttributeValue): RelationalOperator(attribute, substr) {
    override val condition: String get() = "begins_with(${attribute.expressionName}, $expressionValue)"
}

internal class And(private val left: Expression, private val right: Expression) : Expression {
    override val condition: String get() = "${left.condition} AND ${right.condition}"
    override val attributeNames: Map<String, String> get() = left.attributeNames + right.attributeNames
    override val attributeValues: Map<String, AttributeValue> get() = left.attributeValues + right.attributeValues
}