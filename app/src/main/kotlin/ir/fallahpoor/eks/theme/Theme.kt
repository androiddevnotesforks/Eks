package ir.fallahpoor.eks.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.fallahpoor.eks.theme.ColorPalette.DarkRed
import ir.fallahpoor.eks.theme.ColorPalette.LightRed
import ir.fallahpoor.eks.theme.ColorPalette.Orange

private object ColorPalette {
    val Blue = Color(0xff2962FF)
    val Orange = Color(0xffFF6D00)
    val LightRed = Color(0xffE57373)
    val DarkRed = Color(0xffF44336)
    val White = Color(0xffEEEEEE)
    val Black = Color(0xff212121)
    val Gray = Color(0xff333333)
}

private val lightColors: Colors = lightColors(
    primary = ColorPalette.Blue,
    primaryVariant = ColorPalette.Blue,
    onPrimary = ColorPalette.White,
    secondary = Orange,
    onSecondary = ColorPalette.White,
    error = LightRed,
    onBackground = ColorPalette.Gray
)

private val darkColors: Colors = darkColors(
    primary = Orange,
    primaryVariant = ColorPalette.Black,
    onPrimary = ColorPalette.White,
    secondary = Orange,
    onSecondary = ColorPalette.White,
    error = DarkRed,
    background = ColorPalette.Gray
)

data class Spacing(
    val default: Dp = 0.dp,
    val verySmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val normal: Dp = 16.dp
)

private val LocalSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current


@Composable
fun ReleaseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colors = if (darkTheme) darkColors else lightColors,
            content = content
        )
    }
}