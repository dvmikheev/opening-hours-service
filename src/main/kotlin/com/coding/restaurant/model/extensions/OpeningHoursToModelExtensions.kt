package com.coding.restaurant.model.extensions

import com.coding.restaurant.model.OpeningHours
import com.coding.restaurant.model.OpeningHoursDTO
import com.coding.restaurant.model.Time
import com.coding.restaurant.model.TimeDTO
import java.util.TreeSet

fun OpeningHoursDTO.toModel() = OpeningHours(
    monday = TreeSet(monday.map { it.toModel() }),
    tuesday = TreeSet(tuesday.map { it.toModel() }),
    wednesday = TreeSet(wednesday.map { it.toModel() }),
    thursday = TreeSet(thursday.map { it.toModel() }),
    friday = TreeSet(friday.map { it.toModel() }),
    saturday = TreeSet(saturday.map { it.toModel() }),
    sunday = TreeSet(sunday.map { it.toModel() })
)

fun TimeDTO.toModel() = Time(
    type = when (type) {
        TimeDTO.Type.OPEN -> Time.Type.OPEN
        TimeDTO.Type.CLOSE -> Time.Type.CLOSE
    },
    value = value
)
