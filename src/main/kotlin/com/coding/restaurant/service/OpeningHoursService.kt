package com.coding.restaurant.service

import com.coding.restaurant.exception.IncorrectEventTypeException
import com.coding.restaurant.exception.IncorrectNumberOfEvents
import com.coding.restaurant.exception.ValuesEqualException
import com.coding.restaurant.model.OpeningHours
import com.coding.restaurant.model.OpeningHoursFormatted
import com.coding.restaurant.model.Time
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.SortedSet

@Service
class OpeningHoursService {

    companion object {
        private const val DAYS_PER_WEEK = 7
        private const val HOUR = 3600
        private const val MONDAY_INDEX = 0
        private const val TUESDAY_INDEX = 1
        private const val WEDNESDAY_INDEX = 2
        private const val THURSDAY_INDEX = 3
        private const val FRIDAY_INDEX = 4
        private const val SATURDAY_INDEX = 5
        private const val SUNDAY_INDEX = 6
        private val logger = LoggerFactory.getLogger(this::class.java.enclosingClass)
    }

    private lateinit var processedDays: List<MutableList<Time>>

    fun formatSchedule(openingHours: OpeningHours): OpeningHoursFormatted {
        processedDays = List(DAYS_PER_WEEK) { mutableListOf() }

        logger.info("Starting days processing...")
        processDay(openingHours.monday, MONDAY_INDEX)
        processDay(openingHours.tuesday, TUESDAY_INDEX)
        processDay(openingHours.wednesday, WEDNESDAY_INDEX)
        processDay(openingHours.thursday, THURSDAY_INDEX)
        processDay(openingHours.friday, FRIDAY_INDEX)
        processDay(openingHours.saturday, SATURDAY_INDEX)
        processDay(openingHours.sunday, SUNDAY_INDEX)

        logger.info("Starting days formatting...")
        return OpeningHoursFormatted(
            processedDays[MONDAY_INDEX].formatDay(),
            processedDays[TUESDAY_INDEX].formatDay(),
            processedDays[WEDNESDAY_INDEX].formatDay(),
            processedDays[THURSDAY_INDEX].formatDay(),
            processedDays[FRIDAY_INDEX].formatDay(),
            processedDays[SATURDAY_INDEX].formatDay(),
            processedDays[SUNDAY_INDEX].formatDay()
        )
    }

    private fun processDay(events: SortedSet<Time>, idx: Int) {
        logger.info("Processing day with index $idx")
        if (!events.isEmpty()) {
            events.first().let { event ->
                if (event.type == Time.Type.CLOSE) {
                    processedDays[(processedDays.size + idx - 1) % processedDays.size] += event
                    events.remove(event)
                }
            }
        }
        if (events.isEmpty()) {
            processedDays[idx] += mutableListOf()
        } else if (events.groupBy { it.value }.values.first().size > 1) {
            val message = "Cannot open and close at the same time"
            logger.error("Error occurred while processing day: $message")
            throw ValuesEqualException(message)
        }

        processedDays[idx] += events.toMutableList()
    }

    private fun List<Time>.formatDay(): String {
        if (this.size % 2 != 0) {
            val message = "Incorrect amount of events provided"
            logger.error("Error occurred while formatting day: $message")
            throw IncorrectNumberOfEvents(message)
        }
        return if (this.isEmpty()) {
            "Closed"
        } else {
            val listOfPairs = this.windowed(2, 2).map { Pair(it[0], it[1]) }
            val intervals = listOfPairs.map { interval -> formatInterval(interval.first, interval.second) }
            intervals.joinToString()
        }
    }

    private fun formatInterval(from: Time, to: Time): String {
        if (from.type != Time.Type.OPEN || to.type != Time.Type.CLOSE) {
            val message = "Types of events are not correct"
            logger.error("Error occurred while processing time: $message")
            throw IncorrectEventTypeException(message)
        }
        return "${from.value.formatTime()} - ${to.value.formatTime()}"
    }

    private fun Int.formatTime(): String = OffsetDateTime
        .ofInstant(Instant.ofEpochSecond(this.toLong()), ZoneOffset.UTC)
        .format(
            DateTimeFormatter.ofPattern(
                if (this % HOUR == 0)
                    "h a"
                else
                    "h:m a"
            )
        )
}
