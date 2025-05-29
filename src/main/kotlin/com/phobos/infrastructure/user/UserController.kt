package com.phobos.infrastructure.user

import com.phobos.infrastructure.util.PageableUtil
import com.phobos.application.user.GetUserMentalDisorder
import com.phobos.application.user.GetUsers
import com.phobos.application.user.UserQueryService
import com.phobos.infrastructure.mentaldisorder.MentalDisorderResponse
import com.phobos.infrastructure.mentaldisorder.toResponse
import com.phobos.infrastructure.security.JwtUtil
import com.phobos.infrastructure.security.PhobosUserDetails
import com.phobos.infrastructure.user.dto.UserResponse
import com.phobos.infrastructure.user.dto.toResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController(
    private val getUsers: GetUsers,
    private val jwtUtil: JwtUtil,
    private val userQueryService: UserQueryService,
    private val getUserMentalDisorder: GetUserMentalDisorder
) {

    @GetMapping
    fun getAllUsers(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) mentalDisorder: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "name,asc") sort: String,
    ): ResponseEntity<Page<UserResponse>> {
        val pageable: Pageable = PageableUtil.getPageableFrom(page, size, sort)
        val userPage = getUsers.execute(name, mentalDisorder, pageable)
        return ResponseEntity.ok(userPage.map { it.toResponse() })
    }

    @GetMapping("/my-user")
    fun getMyUser(@AuthenticationPrincipal userDetails: PhobosUserDetails): ResponseEntity<UserResponse> {
        return try {
            val email = userDetails.username
            val userResponse = userQueryService.getUserByEmail(email)
            ResponseEntity.ok(userResponse)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


    @GetMapping("/{userId}/mental-disorder")
    fun getUserMentalDisorder(
        @PathVariable(value = "userId", required = true) userId: Int
    ): ResponseEntity<List<MentalDisorderResponse>> {
        val response = getUserMentalDisorder.execute(userId)
        return ResponseEntity.ok(response.map { it.toResponse() })
    }
//
//    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
//    fun createUser(
//        @RequestPart userRequest: UserRequest,
//        @RequestParam("image", required = false) image: MultipartFile?
//    ): ResponseEntity<Void> {
//        userService.createUser(userRequest, image)
//        return ResponseEntity.ok().build()
//    }
}