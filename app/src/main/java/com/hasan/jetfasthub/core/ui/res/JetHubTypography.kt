package com.hasan.jetfasthub.core.ui.res

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import com.hasan.jetfasthub.R

/** Old Typography
// Set of Material typography styles to start with

private val fonts = FontFamily(
    Font(R.font.sailec_regular),
    Font(R.font.sailec_medium, FontWeight.W500),
    Font(R.font.sailec_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val typography = androidx.compose.material3.Typography(
    headlineLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W600,
        fontSize = 30.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = fonts,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    )
)
*/

private val RobotoFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
)

val JetHubTypography = Typography(
    defaultFontFamily = RobotoFamily,
    h1 = TextStyle(
        fontSize = 34.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(value = 0f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 44f, type = TextUnitType.Sp),
    ),
    h2 = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(value = 0.18f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 32f, type = TextUnitType.Sp),
    ),
    h3 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(value = 0.15f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 24f, type = TextUnitType.Sp),
    ),
    subtitle1 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(value = 0.15f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 24f, type = TextUnitType.Sp),
    ),
    subtitle2 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(value = 0.5f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 24f, type = TextUnitType.Sp),
    ),
    body1 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(value = 0.5f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 24f, type = TextUnitType.Sp),
    ),
    body2 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(value = 0.25f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 20f, type = TextUnitType.Sp),
    ),
    button = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(value = 0.1f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16f, type = TextUnitType.Sp),
    ),
    caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = TextUnit(value = 0.4f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16f, type = TextUnitType.Sp),
    ),
    overline = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = TextUnit(value = 1.5f, type = TextUnitType.Sp),
        lineHeight = TextUnit(value = 16f, type = TextUnitType.Sp),
    ),
)
