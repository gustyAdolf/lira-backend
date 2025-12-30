package com.lira.infrastructure.reportType

import com.lira.domain.reportType.ReportType
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
    val name: String
)

fun ReportTypeEntity.toDomain(): ReportType = ReportType(
    id = this.id,
    name = this.name
)