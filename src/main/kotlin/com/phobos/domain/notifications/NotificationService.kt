package com.phobos.domain.notifications

interface NotificationService {
    fun sendNotification(token: String, title: String, body: String)
}