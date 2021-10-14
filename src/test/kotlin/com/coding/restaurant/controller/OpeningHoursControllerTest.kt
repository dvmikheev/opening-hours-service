package com.coding.restaurant.controller

import com.coding.restaurant.model.OpeningHoursDTO
import com.coding.restaurant.model.TimeDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringJUnitConfig
@SpringBootTest
@AutoConfigureMockMvc
class OpeningHoursControllerTest {

    companion object {
        private const val BASE_PATH = "/restaurants"
        private val objectMapper = jacksonObjectMapper()
        val unformattedDataTemplate = OpeningHoursDTO(
            monday = listOf(),
            tuesday = listOf(),
            wednesday = listOf(),
            thursday = listOf(),
            friday = listOf(),
            saturday = listOf(),
            sunday = listOf()
        )
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should format correctly when opens on monday and closes on tuesday`() {
        val requestData = unformattedDataTemplate.copy(
            monday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 32400)
            ),
            tuesday = listOf(
                TimeDTO(type = TimeDTO.Type.CLOSE, 3600),
                TimeDTO(type = TimeDTO.Type.OPEN, 28800),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            )
        )
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    string(
                        getOpeningHoursResult(monday = "9 AM - 1 AM", tuesday = "8 AM - 8 PM")
                    )
                }
            }
    }

    @Test
    fun `should format correctly when opens one day and closes next day`() {
        val requestData = unformattedDataTemplate.copy(
            monday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 32400)
            ),
            tuesday = listOf(
                TimeDTO(type = TimeDTO.Type.CLOSE, 3600)
            )
        )
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    string(
                        getOpeningHoursResult(monday = "9 AM - 1 AM")
                    )
                }
            }
    }

    @Test
    fun `should format correctly when opens and closes same day`() {
        val requestData = unformattedDataTemplate.copy(
            monday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 32400),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            ),
            tuesday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 32400),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            )
        )
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    string(
                        getOpeningHoursResult(monday = "9 AM - 8 PM", tuesday = "9 AM - 8 PM")
                    )
                }
            }
    }

    @Test
    fun `should format correctly when opens and closes multiple times`() {
        val requestData = unformattedDataTemplate.copy(
            monday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 28800),
                TimeDTO(type = TimeDTO.Type.CLOSE, 43200),
                TimeDTO(type = TimeDTO.Type.OPEN, 46800),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            ),
            tuesday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 32400),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            )
        )
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    string(
                        getOpeningHoursResult(monday = "8 AM - 12 PM, 1 PM - 8 PM", tuesday = "9 AM - 8 PM")
                    )
                }
            }
    }

    @Test
    fun `should format schedule correctly`() {
        val requestData = unformattedDataTemplate.copy(
            monday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 28800),
                TimeDTO(type = TimeDTO.Type.CLOSE, 43200),
                TimeDTO(type = TimeDTO.Type.OPEN, 46800),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            ),
            tuesday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 32400)
            ),
            wednesday = listOf(
                TimeDTO(type = TimeDTO.Type.CLOSE, 3600),
                TimeDTO(type = TimeDTO.Type.OPEN, 32400),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            ),
            thursday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 32400),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            ),
            friday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 32400),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            ),
            saturday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 32400),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            )
        )
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content {
                    string(
                        getOpeningHoursResult(
                            monday = "8 AM - 12 PM, 1 PM - 8 PM",
                            tuesday = "9 AM - 1 AM",
                            wednesday = "9 AM - 8 PM",
                            thursday = "9 AM - 8 PM",
                            friday = "9 AM - 8 PM",
                            saturday = "9 AM - 8 PM"
                        )
                    )
                }
            }
    }

    @Test
    fun `should fail when opens and closes at the same time`() {
        val requestData = unformattedDataTemplate.copy(
            monday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 72000),
                TimeDTO(type = TimeDTO.Type.CLOSE, 72000)
            )
        )
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content {
                    jsonPath("$.code") { value("Bad Request") }
                    jsonPath("$.message") { value("Cannot open and close at the same time") }
                }
            }
    }

    @Test
    fun `should fail when incorrect amount of events per day provided`() {
        val requestData = unformattedDataTemplate.copy(
            monday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 3600),
                TimeDTO(type = TimeDTO.Type.CLOSE, 28800),
                TimeDTO(type = TimeDTO.Type.OPEN, 72000)
            )
        )
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content {
                    jsonPath("$.code") { value("Bad Request") }
                    jsonPath("$.message") { value("Incorrect amount of events provided") }
                }
            }
    }

    @Test
    fun `should fail when event type is incorrect`() {
        val requestData = unformattedDataTemplate.copy(
            monday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 3600),
                TimeDTO(type = TimeDTO.Type.OPEN, 72000)
            )
        )
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content {
                    jsonPath("$.code") { value("Bad Request") }
                    jsonPath("$.message") { value("Types of events are not correct") }
                }
            }
    }

    @Test
    fun `should fail when value exceeds limits`() {
        val requestData = unformattedDataTemplate.copy(
            monday = listOf(
                TimeDTO(type = TimeDTO.Type.OPEN, 3600),
                TimeDTO(type = TimeDTO.Type.OPEN, 100000)
            )
        )
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content {
                    jsonPath("$.code") { value("Bad Request") }
                    jsonPath("$.message") { value("Invalid value in payload") }
                }
            }
    }

    @Test
    fun `should fail when incorrect json payload provided`() {
        val requestData = "{}"
        this.mockMvc.post("$BASE_PATH/opening-hours") {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            content = objectMapper.writeValueAsString(requestData)
        }
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
                content {
                    jsonPath("$.code") { value("Bad Request") }
                    jsonPath("$.message") { value("Json parse exception") }
                }
            }
    }

    private fun getOpeningHoursResult(
        monday: String = "Closed",
        tuesday: String = "Closed",
        wednesday: String = "Closed",
        thursday: String = "Closed",
        friday: String = "Closed",
        saturday: String = "Closed",
        sunday: String = "Closed"
    ) = "Monday: $monday\n" +
        "Tuesday: $tuesday\n" +
        "Wednesday: $wednesday\n" +
        "Thursday: $thursday\n" +
        "Friday: $friday\n" +
        "Saturday: $saturday\n" +
        "Sunday: $sunday\n"
}
