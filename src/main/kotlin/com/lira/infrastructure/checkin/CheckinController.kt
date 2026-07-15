package com.lira.infrastructure.checkin

import com.lira.application.checkin.*
import com.lira.infrastructure.checkin.dto.toResponse
import com.lira.infrastructure.checkin.dto.CheckinRequest
import com.lira.infrastructure.checkin.dto.CheckinResponse
import com.lira.infrastructure.checkin.dto.CheckoutRequest
import com.lira.infrastructure.checkin.dto.CompanyCheckinSummaryResponse
import com.lira.infrastructure.checkin.dto.ManualCheckinRequest
import com.lira.infrastructure.checkin.dto.NewCheckinRequest
import com.lira.infrastructure.checkin.pdf.CheckinPdfService
import com.lira.infrastructure.rest.ApiResponse
import com.lira.infrastructure.rest.ApiResponseStatus
import com.lira.infrastructure.security.LiraUserDetails
import com.lira.domain.user.UserRepository
import com.lira.domain.user.UserType
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/checkins")
@PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST')")
class CheckinController(
    private val checkin: Checkin,
    private val checkout: Checkout,
    private val getWorkDay: GetWorkDay,
    private val getCheckinHistory: GetCheckinHistory,
    private val updateCheckin: UpdateCheckin,
    private val deleteCheckin: DeleteCheckin,
    private val createManualCheckin: CreateManualCheckin,
    private val getCompanyTherapistCheckins: GetCompanyTherapistCheckins,
    private val checkinPdfService: CheckinPdfService,
    private val userRepository: UserRepository
) {

    @GetMapping("/user/{userId}/workday")
    fun getWorkDay(
        @PathVariable userId: Int,
        @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") date: LocalDate = LocalDate.now(),
        @RequestParam(required = false) companyId: Int?
    ): List<CheckinResponse> {
        return getWorkDay.execute(userId, date, companyId).map { it.toResponse() }
    }

    @GetMapping("/user/{userId}")
    fun gethistory(
        @PathVariable userId: Int,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") from: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") to: LocalDate?
    ): List<CheckinResponse> {
        return getCheckinHistory.execute(userId, from, to).map { it.toResponse() }
    }

    @PostMapping("/checkin")
    fun checkinTherapist(@RequestBody checkinRequest: NewCheckinRequest): ApiResponse<Unit> {
        checkin.execute(checkinRequest)
        return ApiResponse(ApiResponseStatus.SUCCESS, "Check-in realizado correctamente")
    }

    @PostMapping("/manual")
    @ResponseStatus(HttpStatus.CREATED)
    fun createManual(@RequestBody request: ManualCheckinRequest): CheckinResponse {
        return createManualCheckin.execute(request).toResponse()
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

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','THERAPIST')")
    fun getCompanyTherapistCheckins(
        @PathVariable companyId: Int,
        @RequestParam therapistId: Int,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") from: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") to: LocalDate?,
        @AuthenticationPrincipal userDetails: LiraUserDetails
    ): CompanyCheckinSummaryResponse {
        ensureCanAccessCompany(userDetails, companyId)
        return getCompanyTherapistCheckins.execute(companyId, therapistId, from, to).toResponse()
    }

    @GetMapping("/company/{companyId}/pdf", produces = [MediaType.APPLICATION_PDF_VALUE])
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','THERAPIST')")
    fun exportCompanyTherapistCheckinsPdf(
        @PathVariable companyId: Int,
        @RequestParam therapistId: Int,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") from: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") to: LocalDate?,
        @AuthenticationPrincipal userDetails: LiraUserDetails
    ): ResponseEntity<ByteArray> {
        ensureCanAccessCompany(userDetails, companyId)
        val summary = getCompanyTherapistCheckins.execute(companyId, therapistId, from, to)
        val bytes = checkinPdfService.generate(companyId, therapistId, from!!, to!!, summary)
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_PDF
            setContentDispositionFormData("inline", "fichajes_$therapistId.pdf")
        }
        return ResponseEntity(bytes, headers, HttpStatus.OK)
    }

    /**
     * Una cuenta de empresa solo puede consultar su propia clínica (su `id` es el
     * mismo `companyId`). Un terapeuta con clínica propia (multi-clínica, toggle de
     * rol) puede consultar la clínica que gestiona aunque su cuenta sea THERAPIST,
     * no COMPANY — se verifica contra `user_company` igual que el resto del rol empresa.
     */
    private fun ensureCanAccessCompany(userDetails: LiraUserDetails, companyId: Int) {
        val user = userDetails.user
        if (user.id == companyId) return
        if (user.userType == UserType.THERAPIST &&
            userRepository.findTherapistsByCompanyId(companyId).any { it.id == user.id }
        ) {
            return
        }
        throw AccessDeniedException("No puedes consultar los fichajes de esta clínica")
    }

}