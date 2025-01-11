package com.phobos.controller

import com.phobos.application.config.security.JwtUtil
import com.phobos.application.dto.MentalDisorderResponse
import com.phobos.application.dto.user.UserRequest
import com.phobos.application.dto.user.UserResponse
import com.phobos.application.service.UserService
import com.phobos.application.util.PageableUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil
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
        val userPage: Page<UserResponse> = userService.getUsers(name, mentalDisorder, pageable)
        return ResponseEntity.ok(userPage)
    }

    @GetMapping("/my-user")
    fun getMyUser(request: HttpServletRequest): ResponseEntity<UserResponse> {
        try {
            val authorizationHeader = request.getHeader("Authorization")
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }

            val token = authorizationHeader.substring(7)
            val email = jwtUtil.extractUsername(token)
            return ResponseEntity.ok(userService.getUserByEmail(email))
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/{userId}/mental-disorder")
    fun getUserMentalDisorder(
        @PathVariable(value = "userId", required = true) userId: Int
    ): ResponseEntity<List<MentalDisorderResponse>> {
        return ResponseEntity.ok(userService.getMentalDisordersByUserId(userId))
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createUser(
        @RequestPart userRequest: UserRequest,
        @RequestParam("image", required = false) image: MultipartFile?
    ): ResponseEntity<Void> {
        userService.createUser(userRequest, image)
        return ResponseEntity.ok().build()
    }
}