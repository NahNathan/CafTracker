package com.nathanrds.caftracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CoffeeOrangeLight,
    onPrimary = CoffeeEspresso,
    primaryContainer = CoffeeEspressoSurfaceVariant,
    onPrimaryContainer = CoffeeCreamText,
    secondary = CoffeeOrange,
    onSecondary = Color.White,
    secondaryContainer = CoffeeEspressoSurfaceVariant,
    onSecondaryContainer = CoffeeCreamText,
    tertiary = CoffeeOrangeDark,
    onTertiary = Color.White,
    background = CoffeeEspressoBackground,
    onBackground = CoffeeCreamText,
    surface = CoffeeEspressoSurface,
    onSurface = CoffeeCreamText,
    surfaceVariant = CoffeeEspressoSurfaceVariant,
    onSurfaceVariant = CoffeeCreamMuted,
    outline = CoffeeEspressoDivider,
    outlineVariant = CoffeeEspressoDivider,
    error = Color(0xFFFFB4A9),
    onError = Color(0xFF680003),
    errorContainer = Color(0xFF930006),
    onErrorContainer = Color(0xFFFFDAD4)
)

private val LightColorScheme = lightColorScheme(
    primary = CoffeeOrange,
    onPrimary = Color.White,
    primaryContainer = CoffeeContainerSoft,
    onPrimaryContainer = CoffeeEspresso,
    secondary = CoffeeOrangeDark,
    onSecondary = Color.White,
    secondaryContainer = CoffeeDivider,
    onSecondaryContainer = CoffeeEspresso,
    tertiary = CoffeeOrangeLight,
    onTertiary = CoffeeEspresso,
    background = CoffeeCreamBackground,
    onBackground = CoffeeTextPrimary,
    surface = CoffeeCreamSurface,
    onSurface = CoffeeTextPrimary,
    surfaceVariant = CoffeeCreamSurfaceVariant,
    onSurfaceVariant = CoffeeTextMuted,
    outline = CoffeeDivider,
    outlineVariant = CoffeeDivider,
    error = CoffeeError,
    onError = Color.White,
    errorContainer = CoffeeErrorContainer,
    onErrorContainer = CoffeeOnErrorContainer
)

@Composable
fun CafTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Identidade visual própria (café/laranja): cor dinâmica do Material You fica desativada
    // por padrão para preservar a aparência da marca em qualquer aparelho.
    dynamicColor: Boolean = false,
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
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
