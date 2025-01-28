package fr.uha.chaguer.trainy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.uha.chaguer.trainy.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

val MontserratFont = FontFamily(
    Font(R.font.montserrat_alternates_black, FontWeight.Black),
    Font(R.font.montserrat_alternates_black_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.montserrat_alternates_bold, FontWeight.Bold),
    Font(R.font.montserrat_alternates_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.montserrat_alternates_light, FontWeight.Light),
    Font(R.font.montserrat_alternates_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.montserrat_alternates_medium, FontWeight.Medium),
    Font(R.font.montserrat_alternates_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.montserrat_alternates_regular, FontWeight.Normal),
    Font(R.font.montserrat_alternates_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_alternates_semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.montserrat_alternates_thin, FontWeight.Thin),
    Font(R.font.montserrat_alternates_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.montserrat_alternates_extrabold, FontWeight.ExtraBold),
    Font(R.font.montserrat_alternates_extrabold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.montserrat_alternates_extralight, FontWeight.ExtraLight),
    Font(R.font.montserrat_alternates_extralight_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.montserrat_alternates_italic, FontWeight.Normal, FontStyle.Italic)
)

@Composable
fun TrainyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme(
            primary = Color(0xFF4A148C),
            secondary = Color(0xFFF57C00),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.White,
            onSecondary = Color.Black
        )
        else -> lightColorScheme(
            primary = Color(0xFF4A148C),
            secondary = Color(0xFFF57C00),
            background = Color(0xFFF5F5F5),
            surface = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.Black
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            titleLarge = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_alternates_black)),
                fontSize = 22.sp
            ),
            bodyLarge = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_alternates_light)),
                fontSize = 16.sp
            )
        ),
        content = content
    )
}