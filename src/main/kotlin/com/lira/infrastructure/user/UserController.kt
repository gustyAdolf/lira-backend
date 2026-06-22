package com.lira.infrastructure.user

import com.lira.application.user.CreateUser
import com.lira.application.user.GetUserMentalDisorder
import com.lira.application.user.GetUsers
import com.lira.application.user.UserQueryService
import com.lira.domain.user.UserQueryType
import com.lira.infrastructure.user.dto.toResponse
import com.lira.infrastructure.mentaldisorder.MentalDisorderResponse
import com.lira.infrastructure.mentaldisorder.toResponse
import com.lira.infrastructure.security.LiraUserDetails
import com.lira.infrastructure.user.dto.UserRequest
import com.lira.infrastructure.user.dto.UserResponse
import com.lira.infrastructure.util.PageableUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/users")
class UserController(
    private val getUsers: GetUsers,
    private val userQueryService: UserQueryService,
    private val getUserMentalDisorder: GetUserMentalDisorder,
    private val createUser: CreateUser
) {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun getAllUsers(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) mentalDisorder: String?,
        @RequestParam(required = false) userType: UserQueryType = UserQueryType.PATIENT,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "name,asc") sort: String,
    ): ResponseEntity<Page<UserResponse>> {
        val pageable: Pageable = PageableUtil.getPageableFrom(page, size, sort)
        val userPage = getUsers.execute(name, mentalDisorder, userType, pageable)
        return ResponseEntity.ok(userPage.map { it.toResponse() })
    }

    @GetMapping("/my-user")
    @PreAuthorize("isAuthenticated()")
    fun getMyUser(@AuthenticationPrincipal userDetails: LiraUserDetails): ResponseEntity<UserResponse> {
        return try {
            val email = userDetails.username
            val user = userQueryService.getUserByEmail(email)
            ResponseEntity.ok(user.toResponse())
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


    @GetMapping("/{userId}/mental-disorder")
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','PATIENT')")
    fun getUserMentalDisorder(
        @PathVariable(value = "userId", required = true) userId: Int
    ): ResponseEntity<List<MentalDisorderResponse>> {
        val response = getUserMentalDisorder.execute(userId)
        return ResponseEntity.ok(response.map { it.toResponse() })
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @PreAuthorize("hasAnyAuthority('ADMIN','THERAPIST','COMPANY')")
    fun createUser(
        @RequestPart userRequest: UserRequest,
        @RequestParam("image", required = false) image: MultipartFile?
    ): ResponseEntity<Void> {
        createUser.execute(userRequest, image)
        return ResponseEntity.ok().build()
    }
}