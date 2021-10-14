package com.coding.restaurant.controller

import com.coding.restaurant.exception.IncorrectEventTypeException
import com.coding.restaurant.exception.IncorrectNumberOfEvents
import com.coding.restaurant.exception.ValuesEqualException
import com.coding.restaurant.model.ErrorDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(
        IncorrectEventTypeException::class,
        IncorrectNumberOfEvents::class,
        ValuesEqualException::class
    )
    fun handleHttpMessageNotReadable(exception: Exception): ResponseEntity<Any> {
        return ResponseEntity(
            ErrorDTO(
                HttpStatus.BAD_REQUEST.reasonPhrase, exception.message ?: "Error while processing request"
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(exception: MethodArgumentNotValidException): ResponseEntity<Any> {
        return ResponseEntity(
            ErrorDTO(
                HttpStatus.BAD_REQUEST.reasonPhrase, "Invalid value in payload"
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(exception: HttpMessageNotReadableException): ResponseEntity<Any> {
        return ResponseEntity(
            ErrorDTO(
                HttpStatus.BAD_REQUEST.reasonPhrase, "Json parse exception"
            ),
            HttpStatus.BAD_REQUEST
        )
    }
}
