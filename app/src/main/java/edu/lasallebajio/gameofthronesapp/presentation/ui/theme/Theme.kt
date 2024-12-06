package edu.lasallebajio.gameofthronesapp.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BloodRed,
    secondary = IronGrey,
    tertiary = IceBlue,
    background = NightBlack,
    surface = NightBlack,
    onPrimary = AshWhite,
    onSecondary = AshWhite,
    onTertiary = NightBlack,
    onBackground = AshWhite,
    onSurface = AshWhite
)

private val LightColorScheme = lightColorScheme(
    primary = FireRed,
    secondary = SkyGrey,
    tertiary = FrostBlue,
    background = LightWhite,
    surface = LightWhite,
    onPrimary = LightWhite,
    onSecondary = NightBlack,
    onTertiary = NightBlack,
    onBackground = NightBlack,
    onSurface = NightBlack
)

@Composable
fun GameOfThronesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = globalTypography,
        content = content
    )
}
