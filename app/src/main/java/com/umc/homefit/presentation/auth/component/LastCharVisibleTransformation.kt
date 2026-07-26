package com.umc.homefit.presentation.auth.component

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * 마지막으로 입력한 글자 1개만 원문으로 보여주고 나머지는 마스킹 문자로 가리는
 * VisualTransformation. 글자 수 자체는 변하지 않으므로 OffsetMapping은 identity로 처리한다.
 */
class LastCharVisibleTransformation(
    private val maskChar: Char = '•'
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val masked = if (text.isEmpty()) {
            ""
        } else {
            maskChar.toString().repeat(text.length - 1) + text.last()
        }

        return TransformedText(
            AnnotatedString(masked),
            OffsetMapping.Identity
        )
    }
}
