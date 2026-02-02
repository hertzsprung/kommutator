package uk.co.datumedge.kommutator

sealed interface Attribute {
    val name: String
    val expressionName: String get() = "#$name"
    val expressionValue: String get() = ":$name"

    class Bool(override val name: String) : Attribute {
        infix fun eq(value: Boolean) = Eq(this, value.b())
    }

    class N(override val name: String) : Attribute {
        infix fun eq(value: Number) = Eq(this, value.n())
        infix fun lt(value: Number) = Lt(this, value.n())
        infix fun le(value: Number) = Le(this, value.n())
        infix fun gt(value: Number) = Gt(this, value.n())
        infix fun ge(value: Number) = Ge(this, value.n())
    }

    class S(override val name: String) : Attribute {
        infix fun eq(value: String) = Eq(this, value.s())
        infix fun lt(value: String) = Lt(this, value.s())
        infix fun le(value: String) = Le(this, value.s())
        infix fun gt(value: String) = Gt(this, value.s())
        infix fun ge(value: String) = Ge(this, value.s())
        fun beginsWith(value: String): Expression = BeginsWith(this, value.s())
    }
}