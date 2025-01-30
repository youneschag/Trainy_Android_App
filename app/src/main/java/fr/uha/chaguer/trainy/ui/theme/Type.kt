package fr.uha.chaguer.trainy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import fr.uha.chaguer.trainy.R

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

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp
    )
)