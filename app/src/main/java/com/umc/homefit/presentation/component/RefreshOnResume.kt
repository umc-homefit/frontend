package com.umc.homefit.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * 화면이 다시 보여질 때(ON_RESUME)마다 [onResume]을 호출해 최신 상태로 갱신한다.
 *
 * 이 컴포저블을 감싸고 있는 상위 화면이 다른 화면으로 이동했다가 돌아올 때
 * 완전히 재구성(dispose 후 재생성)되는 경우가 있어서(예: 다른 최상위 화면으로 이동했다가
 * 복귀하는 경우), "최초 진입"과 "진짜 복귀"를 구분하는 방식은 신뢰할 수 없다.
 * 그래서 ON_RESUME마다 항상 [onResume]을 호출한다. 최초 진입 시 ViewModel의 init{}과
 * 중복 호출될 수 있지만, 약간의 추가 네트워크 호출일 뿐 기능적으로는 문제없다.
 */
@Composable
fun RefreshOnResume(onResume: () -> Unit) {
    val currentOnResume = rememberUpdatedState(onResume)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentOnResume.value()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
