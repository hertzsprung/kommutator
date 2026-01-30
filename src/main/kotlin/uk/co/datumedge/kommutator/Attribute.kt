package uk.co.datumedge.kommutator

interface Attribute {
    val name: String
    val expressionName: String get() = "#$name"
    val expressionValue: String get() = ":$name"

    class Bool(override val name: String) : Attribute {
        infix fun eq(value: Boolean) = Eq(this, value.b())
    }

    class N(override val name: String) : Attribute {
        infix fun eq(value: Number) = Eq(this, value.n())
    }

    class S(override val name: String) : Attribute {
        infix fun eq(value: String) = Eq(this, value.s())
    }
}