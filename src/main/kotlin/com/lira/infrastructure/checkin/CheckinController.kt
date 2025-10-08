package com.lira.infrastructure.checkin

import com.lira.application.checkin.*
import com.lira.domain.checkin.toResponse
import com.lira.infrastructure.checkin.dto.CheckinRequest
import com.lira.infrastructure.checkin.dto.CheckinResponse
import com.lira.infrastructure.checkin.dto.CheckoutRequest
import com.lira.infrastructure.checkin.dto.NewCheckinRequest
import com.lira.infrastructure.rest.ApiResponse
import com.lira.infrastructure.rest.ApiResponseStatus
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/checkins")
class CheckinController(
    private val checkin: Checkin,
    private val checkout: Checkout,
    private val getWorkDay: GetWorkDay,
    private val getCheckinHistory: GetCheckinHistory,
    private val updateCheckin: UpdateCheckin,
    private val deleteCheckin: DeleteCheckin
) {

    @GetMapping("/user/{userId}/workday")
    fun getWorkDay(
        @PathVariable userId: Int,
        @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") date: LocalDate = LocalDate.now()
    ): List<CheckinResponse> {
        return getWorkDay.execute(userId, date).map { it.toResponse() }
    }

    @GetMapping("/user/{userId}")
    fun gethistory(@PathVariable userId: Int): List<CheckinResponse> {
        return getCheckinHistory.execute(userId).map { it.toResponse() }
    }

    @PostMapping("/checkin")
    fun checkinTherapist(@RequestBody checkinRequest: NewCheckinRequest): ApiResponse<Unit> {
        checkin.execute(checkinRequest)
        return ApiResponse(ApiResponseStatus.SUCCESS, "Check-in realizado correctamente")
    }

    @PostMapping("/checkout")
    fun checkoutTherapist(@RequestBody checkoutRequest: CheckoutRequest): ApiResponse<Unit> {
        checkout.execute(checkoutRequest)
        return ApiResponse(ApiResponseStatus.SUCCESS, "Check-out realizado correctamente")
    }

    @PutMapping
    fun updateCheckin(@RequestBody checkinRequest: CheckinRequest): ApiResponse<Unit> {
        updateCheckin.execute(checkinRequest)
        return ApiResponse(ApiResponseStatus.SUCCESS, "Check-in actualizado correctamente")
    }

    @DeleteMapping("/{checkinId}")
    fun deleteCheckin(@PathVariable checkinId: Int): ApiResponse<Unit> {
        deleteCheckin.execute(checkinId)
        return ApiResponse(ApiResponseStatus.SUCCESS, "Registro eliminado")
    }

}