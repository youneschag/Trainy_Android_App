package fr.uha.chaguer.trainy.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val PurplePrimary = Color(0xFF4A148C)
val OrangeSecondary = Color(0xFFF57C00)
val DarkBackground = Color(0xFF121212)
val LightBackground = Color(0xFFF5F5F5)
val DarkSurface = Color(0xFF1E1E1E)
val LightSurface = Color.White
val OnPrimaryColor = Color.White
val OnSecondaryColor = Color.Black

val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    secondary = OrangeSecondary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = OnPrimaryColor,
    onSecondary = OnSecondaryColor
)

val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    secondary = OrangeSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = OnPrimaryColor,
    onSecondary = OnSecondaryColor
)