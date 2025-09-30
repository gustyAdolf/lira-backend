package com.phobos.infrastructure.user.jpa

import com.phobos.infrastructure.user.entity.TherapistEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaTherapistRepository : JpaRepository<TherapistEntity, Int> {
}