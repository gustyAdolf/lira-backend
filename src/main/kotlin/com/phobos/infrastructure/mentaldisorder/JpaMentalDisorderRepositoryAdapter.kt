package com.phobos.infrastructure.mentaldisorder

import com.phobos.domain.mentaldisorder.MentalDisorder
import com.phobos.domain.mentaldisorder.MentalDisorderRepository
import org.springframework.stereotype.Repository

@Repository
class JpaMentalDisorderRepositoryAdapter(
    private val jpaMentalDisorderRepository: JpaMentalDisorderRepository
) : MentalDisorderRepository {

    override fun getAllMentalDisorders(): List<MentalDisorder> {

        return jpaMentalDisorderRepository.findAll()
            .map { it.toDomain() }
    }
}