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
 * Lifecycle.addObserver()는 이미 RESUMED 상태인 라이프사이클에 옵저버를 등록하면
 * ON_CREATE/ON_START/ON_RESUME을 즉시 재생(replay)한다. 그래서 화면에 처음 진입할 때도
 * ON_RESUME이 한 번 발생하는데, 이때는 ViewModel의 init{} 로드와 중복 호출되므로 무시하고
 * 그 이후의 "진짜" 복귀(다른 화면 갔다가 돌아오는 경우)에만 [onResume]을 호출한다.
 */
@Composable
fun RefreshOnResume(onResume: () -> Unit) {
    val currentOnResume = rememberUpdatedState(onResume)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        var isFirstResume = true
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (isFirstResume) {
                    isFirstResume = false
                } else {
                    currentOnResume.value()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
