package com.hasan.jetfasthub.core.ui.decorations

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import com.hasan.jetfasthub.core.ui.res.JetHubTheme

fun Modifier.roundedShapeItemDecoration(currentIndex: Int, lastIndex: Int): Modifier = composed {
    val modifierWithHorizontalPadding = this.padding(horizontal = JetHubTheme.dimens.spacing16)
    val isSingleItem = currentIndex == 0 && lastIndex == 0
    when {
        isSingleItem -> {
            modifierWithHorizontalPadding
                .padding(top = JetHubTheme.dimens.spacing14)
                .clip(shape = JetHubTheme.shapes.roundedCornersXMedium)
        }
        currentIndex == 0 -> {
            modifierWithHorizontalPadding
                .padding(top = JetHubTheme.dimens.spacing14)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = JetHubTheme.dimens.radius16,
                        topEnd = JetHubTheme.dimens.radius16,
                    ),
                )
        }
        currentIndex == lastIndex -> {
            modifierWithHorizontalPadding
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = JetHubTheme.dimens.radius16,
                        bottomEnd = JetHubTheme.dimens.radius16,
                    ),
                )
        }
        else -> modifierWithHorizontalPadding
    }
}
