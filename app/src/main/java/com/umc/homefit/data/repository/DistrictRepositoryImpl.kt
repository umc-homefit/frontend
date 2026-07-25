package com.umc.homefit.data.repository

import com.umc.homefit.data.district.SeoulDistricts
import javax.inject.Inject

class DistrictRepositoryImpl @Inject constructor() : DistrictRepository {

    override suspend fun getDistricts(): List<String> {
        return SeoulDistricts.ALL
    }
}
