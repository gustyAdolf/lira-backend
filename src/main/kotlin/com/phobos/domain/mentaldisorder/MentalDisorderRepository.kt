package com.phobos.domain.mentaldisorder

interface MentalDisorderRepository {
    fun getAllMentalDisorders(): List<MentalDisorder>
}