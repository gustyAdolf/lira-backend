package com.phobos.application.mentaldisorder

import com.phobos.domain.mentaldisorder.MentalDisorder
import com.phobos.domain.mentaldisorder.MentalDisorderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetMentalDisorders(
    private val mentalDisorderRepository: MentalDisorderRepository
) {

    fun execute(): List<MentalDisorder> {
        return mentalDisorderRepository.getAllMentalDisorders()
    }
}