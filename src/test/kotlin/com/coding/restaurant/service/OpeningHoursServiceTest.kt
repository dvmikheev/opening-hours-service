package com.coding.restaurant.service

import com.coding.restaurant.exception.IncorrectEventTypeException
import com.coding.restaurant.exception.IncorrectNumberOfEvents
import com.coding.restaurant.exception.ValuesEqualException
import com.coding.restaurant.model.OpeningHours
import com.coding.restaurant.model.OpeningHoursFormatted
import com.coding.restaurant.model.Time
import org.junit.jupiter.api.Test
import java.util.TreeSet
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OpeningHoursServiceTest {
    private val openingHoursService = OpeningHoursService()

    companion object {
        val unformattedDataTemplate = OpeningHours(
            monday = TreeSet(),
            tuesday = TreeSet(),
            wednesday = TreeSet(),
            thursday = TreeSet(),
            friday = TreeSet(),
            saturday = TreeSet(),
            sunday = TreeSet()
        )
    }

    @Test
    fun `should format correctly when opens on monday and closes on tuesday`() {
        val data = unformattedDataTemplate.copy(
            sunday = sortedSetOf(
                Time(type = Time.Type.OPEN, 32400)
            ),
            monday = sortedSetOf(
                Time(type = Time.Type.CLOSE, 3600),
                Time(type = Time.Type.OPEN, 28800),
                Time(type = Time.Type.CLOSE, 72000)
            )
        )
        assertEquals(
            getOpeningHoursResult(sunday = "9 AM - 1 AM", monday = "8 AM - 8 PM"),
            openingHoursService.formatSchedule(data)
        )
    }

    @Test
    fun `should format correctly when opens one day and closes next day`() {
        val data = unformattedDataTemplate.copy(
            monday = sortedSetOf(
                Time(type = Time.Type.OPEN, 32400)
            ),
            tuesday = sortedSetOf(
                Time(type = Time.Type.CLOSE, 72000)
            )
        )
        assertEquals(getOpeningHoursResult(monday = "9 AM - 8 PM"), openingHoursService.formatSchedule(data))
    }

    @Test
    fun `should format correctly when opens and closes same day`() {
        val data = unformattedDataTemplate.copy(
            monday = sortedSetOf(
                Time(type = Time.Type.OPEN, 32400),
                Time(type = Time.Type.CLOSE, 72000)
            ),
            tuesday = sortedSetOf(
                Time(type = Time.Type.OPEN, 32400),
                Time(type = Time.Type.CLOSE, 72000)
            )
        )
        assertEquals(
            getOpeningHoursResult(monday = "9 AM - 8 PM", tuesday = "9 AM - 8 PM"),
            openingHoursService.formatSchedule(data)
        )
    }

    @Test
    fun `should format correctly when opens and closes multiple times`() {
        val data = unformattedDataTemplate.copy(
            monday = sortedSetOf(
                Time(type = Time.Type.OPEN, 28800),
                Time(type = Time.Type.CLOSE, 43200),
                Time(type = Time.Type.OPEN, 46800),
                Time(type = Time.Type.CLOSE, 72000)
            ),
            tuesday = sortedSetOf(
                Time(type = Time.Type.OPEN, 32400),
                Time(type = Time.Type.CLOSE, 72000)
            )
        )
        assertEquals(
            getOpeningHoursResult(monday = "8 AM - 12 PM, 1 PM - 8 PM", tuesday = "9 AM - 8 PM"),
            openingHoursService.formatSchedule(data)
        )
    }

    @Test
    fun `should format schedule correctly`() {
        val data = unformattedDataTemplate.copy(
            monday = sortedSetOf(
                Time(type = Time.Type.OPEN, 28800),
                Time(type = Time.Type.CLOSE, 43200),
                Time(type = Time.Type.OPEN, 46800),
                Time(type = Time.Type.CLOSE, 72000)
            ),
            tuesday = sortedSetOf(
                Time(type = Time.Type.OPEN, 32400)
            ),
            wednesday = sortedSetOf(
                Time(type = Time.Type.CLOSE, 3600),
                Time(type = Time.Type.OPEN, 32400),
                Time(type = Time.Type.CLOSE, 72000)
            ),
            thursday = sortedSetOf(
                Time(type = Time.Type.OPEN, 32400),
                Time(type = Time.Type.CLOSE, 72000)
            ),
            friday = sortedSetOf(
                Time(type = Time.Type.OPEN, 32400),
                Time(type = Time.Type.CLOSE, 72000)
            ),
            saturday = sortedSetOf(
                Time(type = Time.Type.OPEN, 32400),
                Time(type = Time.Type.CLOSE, 72000)
            )
        )
        assertEquals(
            getOpeningHoursResult(
                monday = "8 AM - 12 PM, 1 PM - 8 PM",
                tuesday = "9 AM - 1 AM",
                wednesday = "9 AM - 8 PM",
                thursday = "9 AM - 8 PM",
                friday = "9 AM - 8 PM",
                saturday = "9 AM - 8 PM"
            ),
            openingHoursService.formatSchedule(data)
        )
    }

    @Test
    fun `should fail when opens and closes at the same time`() {
        val data = unformattedDataTemplate.copy(
            monday = sortedSetOf(
                Time(type = Time.Type.OPEN, 72000),
                Time(type = Time.Type.CLOSE, 72000)
            )
        )
        assertFailsWith<ValuesEqualException> { openingHoursService.formatSchedule(data) }
    }

    @Test
    fun `should fail when incorrect amount of events per day provided`() {
        val data = unformattedDataTemplate.copy(
            monday = sortedSetOf(
                Time(type = Time.Type.OPEN, 3600),
                Time(type = Time.Type.CLOSE, 28800),
                Time(type = Time.Type.OPEN, 72000)
            )
        )
        assertFailsWith<IncorrectNumberOfEvents> { openingHoursService.formatSchedule(data) }
    }

    @Test
    fun `should fail when event type is incorrect`() {
        val data = unformattedDataTemplate.copy(
            monday = sortedSetOf(
                Time(type = Time.Type.OPEN, 3600),
                Time(type = Time.Type.OPEN, 72000)
            )
        )
        assertFailsWith<IncorrectEventTypeException> { openingHoursService.formatSchedule(data) }
    }

    private fun getOpeningHoursResult(
        monday: String = "Closed",
        tuesday: String = "Closed",
        wednesday: String = "Closed",
        thursday: String = "Closed",
        friday: String = "Closed",
        saturday: String = "Closed",
        sunday: String = "Closed"
    ) = OpeningHoursFormatted(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
}
