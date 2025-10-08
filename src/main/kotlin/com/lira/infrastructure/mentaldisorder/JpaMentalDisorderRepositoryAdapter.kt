package com.lira.infrastructure.mentaldisorder

import com.lira.domain.mentaldisorder.MentalDisorder
import com.lira.domain.mentaldisorder.MentalDisorderRepository
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