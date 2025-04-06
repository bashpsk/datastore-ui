package io.bash_psk.datastore_demo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.bash_psk.datastore_ui.extension.LocalDatastore
import io.bash_psk.datastore_ui.extension.getPreference
import io.bash_psk.datastore_ui.font.rememberFontRes
import io.bash_psk.datastore_demo.settings.fontEntities

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun DatastoreUITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val dataStore = LocalDatastore.current

    val getSelectedItem by dataStore.getPreference(
        keyString = "FONT-PREFERENCE",
        initial = ""
    ).collectAsStateWithLifecycle(initialValue = "")

    val selectedFontRes by rememberFontRes(id = getSelectedItem, entities = fontEntities)

    val colorScheme = when {

        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val Typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = when (selectedFontRes != null) {

                true -> FontFamily(Font(resId = selectedFontRes!!))
                false -> FontFamily.Default
            },
            fontWeight = TypographyTokens.BodyLargeWeight,
            fontSize = TypographyTokens.BodyLargeSize,
            lineHeight = TypographyTokens.BodyLargeLineHeight,
            letterSpacing = TypographyTokens.BodyLargeTracking
        ),
        bodyMedium = TextStyle(
            fontFamily = when (selectedFontRes != null) {

                true -> FontFamily(Font(resId = selectedFontRes!!))
                false -> FontFamily.Default
            },
            fontWeight = TypographyTokens.BodyMediumWeight,
            fontSize = TypographyTokens.BodyMediumSize,
            lineHeight = TypographyTokens.BodyMediumLineHeight,
            letterSpacing = TypographyTokens.BodyMediumTracking
        ),
        bodySmall = TextStyle(
            fontFamily = when (selectedFontRes != null) {

                true -> FontFamily(Font(resId = selectedFontRes!!))
                false -> FontFamily.Default
            },
            fontWeight = TypographyTokens.BodySmallWeight,
            fontSize = TypographyTokens.BodySmallSize,
            lineHeight = TypographyTokens.BodySmallLineHeight,
            letterSpacing = TypographyTokens.BodySmallTracking
        ),
        displayLarge = TextStyle(
            fontFamily = TypographyTokens.DisplayLargeFont,
            fontWeight = TypographyTokens.DisplayLargeWeight,
            fontSize = TypographyTokens.DisplayLargeSize,
            lineHeight = TypographyTokens.DisplayLargeLineHeight,
            letterSpacing = TypographyTokens.DisplayLargeTracking
        ),
        displayMedium = TextStyle(
            fontFamily = TypographyTokens.DisplayMediumFont,
            fontWeight = TypographyTokens.DisplayMediumWeight,
            fontSize = TypographyTokens.DisplayMediumSize,
            lineHeight = TypographyTokens.DisplayMediumLineHeight,
            letterSpacing = TypographyTokens.DisplayMediumTracking
        ),
        displaySmall = TextStyle(
            fontFamily = TypographyTokens.DisplaySmallFont,
            fontWeight = TypographyTokens.DisplaySmallWeight,
            fontSize = TypographyTokens.DisplaySmallSize,
            lineHeight = TypographyTokens.DisplaySmallLineHeight,
            letterSpacing = TypographyTokens.DisplaySmallTracking
        ),
        headlineLarge = TextStyle(
            fontFamily = TypographyTokens.HeadlineLargeFont,
            fontWeight = TypographyTokens.HeadlineLargeWeight,
            fontSize = TypographyTokens.HeadlineLargeSize,
            lineHeight = TypographyTokens.HeadlineLargeLineHeight,
            letterSpacing = TypographyTokens.HeadlineLargeTracking
        ),
        headlineMedium = TextStyle(
            fontFamily = TypographyTokens.HeadlineMediumFont,
            fontWeight = TypographyTokens.HeadlineMediumWeight,
            fontSize = TypographyTokens.HeadlineMediumSize,
            lineHeight = TypographyTokens.HeadlineMediumLineHeight,
            letterSpacing = TypographyTokens.HeadlineMediumTracking
        ),
        headlineSmall = TextStyle(
            fontFamily = TypographyTokens.HeadlineSmallFont,
            fontWeight = TypographyTokens.HeadlineSmallWeight,
            fontSize = TypographyTokens.HeadlineSmallSize,
            lineHeight = TypographyTokens.HeadlineSmallLineHeight,
            letterSpacing = TypographyTokens.HeadlineSmallTracking
        ),
        labelLarge = TextStyle(
            fontFamily = TypographyTokens.LabelLargeFont,
            fontWeight = TypographyTokens.LabelLargeWeight,
            fontSize = TypographyTokens.LabelLargeSize,
            lineHeight = TypographyTokens.LabelLargeLineHeight,
            letterSpacing = TypographyTokens.LabelLargeTracking
        ),
        labelMedium = TextStyle(
            fontFamily = TypographyTokens.LabelMediumFont,
            fontWeight = TypographyTokens.LabelMediumWeight,
            fontSize = TypographyTokens.LabelMediumSize,
            lineHeight = TypographyTokens.LabelMediumLineHeight,
            letterSpacing = TypographyTokens.LabelMediumTracking
        ),
        labelSmall = TextStyle(
            fontFamily = TypographyTokens.LabelSmallFont,
            fontWeight = TypographyTokens.LabelSmallWeight,
            fontSize = TypographyTokens.LabelSmallSize,
            lineHeight = TypographyTokens.LabelSmallLineHeight,
            letterSpacing = TypographyTokens.LabelSmallTracking
        ),
        titleLarge = TextStyle(
            fontFamily = TypographyTokens.TitleLargeFont,
            fontWeight = TypographyTokens.TitleLargeWeight,
            fontSize = TypographyTokens.TitleLargeSize,
            lineHeight = TypographyTokens.TitleLargeLineHeight,
            letterSpacing = TypographyTokens.TitleLargeTracking
        ),
        titleMedium = TextStyle(
            fontFamily = TypographyTokens.TitleMediumFont,
            fontWeight = TypographyTokens.TitleMediumWeight,
            fontSize = TypographyTokens.TitleMediumSize,
            lineHeight = TypographyTokens.TitleMediumLineHeight,
            letterSpacing = TypographyTokens.TitleMediumTracking
        ),
        titleSmall = TextStyle(
            fontFamily = TypographyTokens.TitleSmallFont,
            fontWeight = TypographyTokens.TitleSmallWeight,
            fontSize = TypographyTokens.TitleSmallSize,
            lineHeight = TypographyTokens.TitleSmallLineHeight,
            letterSpacing = TypographyTokens.TitleSmallTracking
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}