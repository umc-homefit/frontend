package com.umc.homefit.data.local

interface TokenProvider {
    fun getAccessToken(): String?
}
