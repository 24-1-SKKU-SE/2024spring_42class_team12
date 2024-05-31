package com.skku.fixskkufront

data class ReportResponse(
    val code: Int,
    val message: String,
    val data: ReportData
)

data class ReportData(
    val page: Int,
    val totalPages: Int,
    val reports: List<Report>
)

data class Report(
    val reportId: Int,
    val facilityId: String,
    val token: String,
    val description: String,
    val photoUrl: String,
    val reportStatus: String,
    val creationDate: String,
    val rejectionReason: String,
    val campus: String,
    val building: String,
    val floor: String,
    val classroom: String,
    val facilityType: String,
    val facilityStatus: String
)