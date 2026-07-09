package com.lira.domain.exceptions

sealed class NotificationException(message: String) : RuntimeException(message) {
    class NoNotificationExists : NotificationException("No existe la notificación")
}
