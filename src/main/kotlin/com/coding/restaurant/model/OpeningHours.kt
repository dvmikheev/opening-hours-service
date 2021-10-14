package com.coding.restaurant.model

import java.util.TreeSet

data class OpeningHours(
    val monday: TreeSet<Time>,
    val tuesday: TreeSet<Time>,
    val wednesday: TreeSet<Time>,
    val thursday: TreeSet<Time>,
    val friday: TreeSet<Time>,
    val saturday: TreeSet<Time>,
    val sunday: TreeSet<Time>
)
