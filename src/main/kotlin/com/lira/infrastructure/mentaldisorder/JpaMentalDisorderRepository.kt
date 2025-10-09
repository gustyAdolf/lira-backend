package com.lira.infrastructure.mentaldisorder

import org.springframework.data.jpa.repository.JpaRepository

interface JpaMentalDisorderRepository : JpaRepository<MentalDisorderEntity, Int> {
}