package com.umc.homefit.data.repository.recruitment

import com.umc.homefit.data.repository.recruitment.SeoulDistricts
import com.umc.homefit.domain.repository.recruitment.DistrictRepository
import javax.inject.Inject

class DistrictRepositoryImpl @Inject constructor() : DistrictRepository {

    override suspend fun getDistricts(): List<String> {
        return SeoulDistricts.ALL
    }
}
