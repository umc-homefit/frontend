package com.umc.homefit.util.error

enum class ErrorCode(val code: String) {
    COMMON400("COMMON400"),
    AUTH401("AUTH401"),
    COMMON404("COMMON404"),
    COMMON409("COMMON409"),
    AUTH409("AUTH409"),
    FINANCE400("FINANCE400"),
    FINANCE404("FINANCE404"),
    COMMON500("COMMON500"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun from(code: String): ErrorCode = entries.find { it.code == code } ?: UNKNOWN
    }
}
