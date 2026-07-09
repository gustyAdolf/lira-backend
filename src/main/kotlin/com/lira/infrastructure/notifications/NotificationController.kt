package com.lira.infrastructure.notifications

import com.lira.application.notifications.GetUserNotifications
import com.lira.application.notifications.MarkNotificationAsRead
import com.lira.infrastructure.notifications.dto.NotificationResponse
import com.lira.infrastructure.notifications.dto.toResponse
import com.lira.infrastructure.security.LiraUserDetails
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val getUserNotifications: GetUserNotifications,
    private val markNotificationAsRead: MarkNotificationAsRead,
) {
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','PATIENT','THERAPIST','COMPANY')")
    fun getMyNotifications(
        @AuthenticationPrincipal userDetails: LiraUserDetails,
    ): List<NotificationResponse> =
        getUserNotifications.execute(userDetails.user.id).map { it.toResponse() }

    @PostMapping("/{notificationId}/read")
    @PreAuthorize("hasAnyAuthority('ADMIN','PATIENT','THERAPIST','COMPANY')")
    fun markAsRead(
        @PathVariable notificationId: Int,
        @AuthenticationPrincipal userDetails: LiraUserDetails,
    ): NotificationResponse =
        markNotificationAsRead.execute(notificationId, userDetails.user.id).toResponse()
}
