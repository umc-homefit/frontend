package com.umc.homefit.data.repository

interface DistrictRepository {
    suspend fun getDistricts(): List<String>
}
