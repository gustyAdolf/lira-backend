package com.lira.infrastructure.util

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

object PageableUtil {

    fun getPageableFrom(page: Int, size: Int, sort: String): Pageable {
        val sortParams = sort.split(",")
        val sortField = sortParams[0]
        val sortDirection = if (sortParams.size > 1 && sortParams[1].equals("desc")) {
            Sort.Direction.DESC
        } else {
            Sort.Direction.ASC
        }
        return PageRequest.of(page, size, Sort.by(sortDirection, sortField))
    }

    fun getSort(direction: String, sortBy: String): Sort {
        val sortDirection = if (direction.uppercase() == "DESC") Sort.Direction.DESC else Sort.Direction.ASC
        return Sort.by(sortDirection, sortBy)
    }
}