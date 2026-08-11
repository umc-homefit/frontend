package com.umc.homefit.data.dto.finance

import kotlinx.serialization.Serializable

@Serializable
data class LoanProductDocumentResponse(
    val documentId: Long,
    val documentName: String,
    val issuer: String? = null,
    val issueMethod: String,
    val documentType: String,
    val isRequired: Boolean
)
