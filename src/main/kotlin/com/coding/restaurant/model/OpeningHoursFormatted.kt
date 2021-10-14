package com.coding.restaurant.model

data class OpeningHoursFormatted(
    private val monday: String = "Closed",
    private val tuesday: String = "Closed",
    private val wednesday: String = "Closed",
    private val thursday: String = "Closed",
    private val friday: String = "Closed",
    private val saturday: String = "Closed",
    private val sunday: String = "Closed"
) {
    override fun toString(): String {
        return "Monday: $monday\n" +
            "Tuesday: $tuesday\n" +
            "Wednesday: $wednesday\n" +
            "Thursday: $thursday\n" +
            "Friday: $friday\n" +
            "Saturday: $saturday\n" +
            "Sunday: $sunday\n"
    }
}
