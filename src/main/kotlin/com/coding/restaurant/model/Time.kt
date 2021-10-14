package com.coding.restaurant.model

data class Time(
    val type: Type,
    val value: Int
) : Comparable<Time> {
    enum class Type(private val value: String) {
        OPEN("open"),
        CLOSE("close");

        override fun toString(): String {
            return this.value
        }
    }

    override fun compareTo(other: Time): Int {
        return when {
            this.type == other.type && this.value == other.value -> 0
            this.value != other.value -> this.value - other.value
            this.type == Type.OPEN -> -1
            else -> 1
        }
    }
}
