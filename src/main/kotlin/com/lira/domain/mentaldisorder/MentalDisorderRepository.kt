package com.lira.domain.mentaldisorder

interface MentalDisorderRepository {
    fun getAllMentalDisorders(): List<MentalDisorder>
}