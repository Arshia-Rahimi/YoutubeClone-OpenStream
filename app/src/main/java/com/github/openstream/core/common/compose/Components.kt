package com.github.openstream.core.common.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun PainterIconButton(
    onClick: () -> Unit,
    @DrawableRes drawableRes: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
) {
    IconButton(
        colors = colors,
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Icon(
            painter = painterResource(drawableRes),
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}
