package com.coding.restaurant.controller

import com.coding.restaurant.model.OpeningHoursDTO
import com.coding.restaurant.model.extensions.toModel
import com.coding.restaurant.service.OpeningHoursService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/restaurants")
class OpeningHoursController(
    val openingHoursService: OpeningHoursService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.enclosingClass)
    }
    @PostMapping("/opening-hours")
    fun getOpeningHours(@Valid @RequestBody openingHours: OpeningHoursDTO): ResponseEntity<String> {
        val formattedSchedule = openingHoursService.formatSchedule(openingHours.toModel()).toString()
        logger.info("Opening hours successfully formatted")
        return ResponseEntity.ok().body(formattedSchedule)
    }
}
