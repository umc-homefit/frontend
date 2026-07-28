package com.umc.homefit.util

import javax.inject.Inject

// TODO: 로그인 기능 완성되면 DataStore 기반 실제 구현체로 교체
class DummyTokenProvider @Inject constructor() : TokenProvider {
    override fun getAccessToken(): String? = null
}
