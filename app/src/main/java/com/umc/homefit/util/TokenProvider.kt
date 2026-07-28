package com.umc.homefit.util

interface TokenProvider {
    fun getAccessToken(): String?
}
