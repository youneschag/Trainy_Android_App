package fr.uha.chaguer.trainy.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

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