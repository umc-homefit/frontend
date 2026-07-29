package com.umc.homefit.util.error

class ApiException(
    val errorCode: ErrorCode,
    val httpStatus: Int? = null,
    override val message: String
) : Exception(message)
