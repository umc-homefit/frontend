package com.umc.homefit.domain.repository.recruitment

interface DistrictRepository {
    suspend fun getDistricts(): List<String>
}
