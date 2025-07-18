package uk.co.datumedge.kommutator

import aws.sdk.kotlin.services.dynamodb.model.AttributeValue

fun Number.n() = AttributeValue.N("$this")
fun String.s() = AttributeValue.S(this)
