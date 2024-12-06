package edu.lasallebajio.gameofthronesapp.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import edu.lasallebajio.gameofthronesapp.R

val gotFontFamily = FontFamily(
    Font(R.font.game_of_thrones)
)

val rubikFontFamily = FontFamily(
    Font(R.font.rubik_regular),
    Font(R.font.rubik_medium),
    Font(R.font.rubik_bold),
    Font(R.font.rubik_light)
)

val globalTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = gotFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 1.sp
    ),
    titleMedium = TextStyle(
        fontFamily = gotFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    titleSmall = TextStyle(
        fontFamily = gotFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = rubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = rubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = rubikFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = rubikFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
