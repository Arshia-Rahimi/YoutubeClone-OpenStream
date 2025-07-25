package com.github.openstream.core.common.compose

import android.text.style.URLSpan
import androidx.annotation.DrawableRes
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.core.text.HtmlCompat

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

@Composable
fun HtmlText(
    html: String,
    onLinkClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    val annotatedString = remember(html) {
        buildAnnotatedString {
            val spanned = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
            
            append(spanned.toString())
            
            val urlSpans = spanned.getSpans(0, spanned.length, URLSpan::class.java)
            for (span in urlSpans) {
                val start = spanned.getSpanStart(span)
                val end = spanned.getSpanEnd(span)
                addStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    ),
                    start = start,
                    end = end,
                )
                addStringAnnotation("URL", span.url, start, end)
            }
        }
    }
    
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    
    Text(
        text = annotatedString,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offsetPosition ->
                    layoutResult?.let { layout ->
                        val position = layout.getOffsetForPosition(offsetPosition)
                        annotatedString.getStringAnnotations("URL", position, position)
                            .firstOrNull()?.let { annotation ->
                                onLinkClicked(annotation.item)
                            }
                    }
                }
            },
        onTextLayout = { layoutResult = it },
        fontSize = fontSize,
        color = color,
    )
}

fun isRtlText(text: String): Boolean {
    val rtlChars = text.count { Character.getDirectionality(it) == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
            Character.getDirectionality(it) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC }
    return rtlChars > text.length / 2
}

@Composable
fun DirectionalText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalTextStyle.current
) {
    val isRtl = remember(text) { isRtlText(text) }
    CompositionLocalProvider(LocalLayoutDirection provides if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        Text(
            text = text,
            modifier = modifier,
            textAlign = if (isRtl) TextAlign.Right else TextAlign.Left,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style,
        )
    }
}
