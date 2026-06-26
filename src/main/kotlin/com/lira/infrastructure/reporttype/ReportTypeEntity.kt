package com.lira.infrastructure.reporttype

import com.lira.domain.reporttype.ReportType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "report_type")
data class ReportTypeEntity(
    @Id
    val id: Int,

    @Column(name = "report_type")
    val name: String,
)

fun ReportTypeEntity.toDomain(): ReportType = ReportType(id = id, name = name)
