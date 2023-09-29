package com.hasan.jetfasthub.core.ui.res

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.material.Typography
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Composable
fun JetFastHubTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    dimens: JetHubDimens = JetHubTheme.dimens,
    typography: Typography = JetHubTheme.typography,
    // Dynamic color is available on Android 12+
    // isDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val themeColors = if (isDarkTheme) darkThemeColors() else lightThemeColors()
    val shapes = remember { JetHubShapes(dimens) }

    val rememberedColors = remember { themeColors }
        .also { it.update(themeColors) }

    /**
    TODO implement dynamic color feature
    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val myColorScheme = when {
    dynamicColor && isDarkTheme -> {
    dynamicDarkColorScheme(LocalContext.current)
    }
    dynamicColor && !isDarkTheme -> {
    dynamicLightColorScheme(LocalContext.current)
    }
    isDarkTheme -> darkThemeColors()
    else -> lightThemeColors()
    }
     */

    //TODO rightly and fully implement status & navigation colors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = themeColors.background.secondary.toArgb()
            window.navigationBarColor = themeColors.background.secondary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !isDarkTheme
            //WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colors = materialThemeColors(colors = themeColors, isDark = isDarkTheme),
        typography = typography,
    ) {
        CompositionLocalProvider(
            LocalJetHubDimens provides dimens,
            LocalJetHubColors provides rememberedColors,
            LocalJetHubTypography provides typography,
            LocalIsInDarkTheme provides isDarkTheme,
            LocalJetHubShapes provides shapes
        ) {
            ProvideTextStyle(
                value = JetHubTheme.typography.body1,
                content = content
            )
        }
    }
}

object JetHubTheme {
    val dimens: JetHubDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalJetHubDimens.current

    val shapes: JetHubShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalJetHubShapes.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalJetHubTypography.current

    val colors: JetHubColors
        @Composable
        @ReadOnlyComposable
        get() = LocalJetHubColors.current
}

@Stable
@Composable
private fun materialThemeColors(colors: JetHubColors, isDark: Boolean): Colors {
    return Colors(
        primary = colors.background.primary,
        primaryVariant = colors.background.secondary,
        secondary = colors.button.primary,
        secondaryVariant = colors.text.accent,
        background = colors.background.primary,
        surface = colors.background.plain,
        error = colors.text.warning,
        onPrimary = colors.text.primary1,
        onSecondary = colors.text.primary1,
        onBackground = colors.text.primary1,
        onSurface = colors.text.primary1,
        onError = colors.text.primary2,
        isLight = !isDark,
    )
}

@Composable
@ReadOnlyComposable
private fun lightThemeColors(): JetHubColors {
    return JetHubColors(
        text = JetHubColors.Text(
            primary1 = JetHubColorPalette.Dark6,
            primary2 = JetHubColorPalette.White,
            secondary = JetHubColorPalette.Dark2,
            tertiary = JetHubColorPalette.Dark1,
            disabled = JetHubColorPalette.Light4,
            warning = JetHubColorPalette.Amaranth,
            attention = JetHubColorPalette.Tangerine,
        ),
        icon = JetHubColors.Icon(
            primary1 = JetHubColorPalette.Black,
            primary2 = JetHubColorPalette.White,
            secondary = JetHubColorPalette.Dark2,
            informative = JetHubColorPalette.Light5,
            inactive = JetHubColorPalette.Light4,
            warning = JetHubColorPalette.Amaranth,
            attention = JetHubColorPalette.Tangerine,
        ),
        button = JetHubColors.Button(
            primary = JetHubColorPalette.Dark6,
            secondary = JetHubColorPalette.Light2,
            disabled = JetHubColorPalette.Light2,
            positiveDisabled = JetHubColorPalette.MagicMint,
        ),
        background = JetHubColors.Background(
            primary = JetHubColorPalette.White,
            secondary = JetHubColorPalette.Light1,
            plain = JetHubColorPalette.White,
            action = JetHubColorPalette.Black,
            fade = JetHubColorPalette.White,
        ),
        control = JetHubColors.Control(
            checked = JetHubColorPalette.Meadow,
            unchecked = JetHubColorPalette.Light2,
            key = JetHubColorPalette.White,
        ),
        stroke = JetHubColors.Stroke(
            primary = JetHubColorPalette.Light2,
            secondary = JetHubColorPalette.Dark4,
            transparency = JetHubColorPalette.White,
        ),
        field = JetHubColors.Field(
            primary = JetHubColorPalette.Light1,
            focused = JetHubColorPalette.Light2,
        ),
    )
}

@Composable
@ReadOnlyComposable
private fun darkThemeColors(): JetHubColors {
    return JetHubColors(
        text = JetHubColors.Text(
            primary1 = JetHubColorPalette.White,
            primary2 = JetHubColorPalette.Dark6,
            secondary = JetHubColorPalette.Light5,
            tertiary = JetHubColorPalette.Dark1,
            disabled = JetHubColorPalette.Dark3,
            warning = JetHubColorPalette.Flamingo,
            attention = JetHubColorPalette.Mustard,
        ),
        icon = JetHubColors.Icon(
            primary1 = JetHubColorPalette.White,
            primary2 = JetHubColorPalette.Dark6,
            secondary = JetHubColorPalette.Dark1,
            informative = JetHubColorPalette.Dark2,
            inactive = JetHubColorPalette.Dark4,
            warning = JetHubColorPalette.Flamingo,
            attention = JetHubColorPalette.Mustard,
        ),
        button = JetHubColors.Button(
            primary = JetHubColorPalette.Light1,
            secondary = JetHubColorPalette.Dark5,
            disabled = JetHubColorPalette.Dark5,
            positiveDisabled = JetHubColorPalette.DarkGreen,
        ),
        background = JetHubColors.Background(
            primary = JetHubColorPalette.Dark6,
            secondary = JetHubColorPalette.Black,
            plain = JetHubColorPalette.Black,
            action = JetHubColorPalette.Light4,
            fade = JetHubColorPalette.Black,
        ),
        control = JetHubColors.Control(
            checked = JetHubColorPalette.Meadow,
            unchecked = JetHubColorPalette.Dark4,
            key = JetHubColorPalette.Light1,
        ),
        stroke = JetHubColors.Stroke(
            primary = JetHubColorPalette.Dark5,
            secondary = JetHubColorPalette.Dark1,
            transparency = JetHubColorPalette.Dark6,
        ),
        field = JetHubColors.Field(
            primary = JetHubColorPalette.Dark5,
            focused = JetHubColorPalette.Dark4,
        ),
    )
}

//TODO think about other best ways
object RippleCustomTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = Color(0xFFC4C7C7),
        lightTheme = true
    )
    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        contentColor = Color.Black,
        lightTheme = true
    )
}

private val LocalJetHubDimens = staticCompositionLocalOf {
    JetHubDimens()
}

private val LocalJetHubTypography = staticCompositionLocalOf {
    JetHubTypography
}

private val LocalIsInDarkTheme = staticCompositionLocalOf {
    false
}

private val LocalJetHubColors = staticCompositionLocalOf<JetHubColors> {
    error("No JetHubColors provided")
}

private val LocalJetHubShapes = staticCompositionLocalOf<JetHubShapes> {
    error("No JetHuShapes provided")
}