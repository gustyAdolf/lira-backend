package com.lira.infrastructure.user.jpa

import com.lira.infrastructure.user.entity.TherapistEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaTherapistRepository : JpaRepository<TherapistEntity, Int> {
}