/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ohoussein.pettoget.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PetAppDarkPalette = darkColors(
    surface = Color(0xFF222831),
    background = Color(0xFF222831),
    primary = Color(0xFF393e46),
    primaryVariant = Color(0xFF00adb5),
    onPrimary = Color(0xFFeeeeee),
    secondary = Color(0xFF004D86),
    secondaryVariant = Color(0xFF29DDFD),
    onSecondary = Color.White,
    onSurface = Color.White,
    onBackground = Color.White,
)

private val PetAppLightPalette = lightColors(
    background = Color(0xFFEEEEEE),
    surface = Color(0xFFF5F5F5),
    primary = Color(0xFF202244),
    primaryVariant = Color(0xFF2B2D51),
    onPrimary = Color.White,
    secondary = Color(0xFF004D86),
    secondaryVariant = Color(0xFF02ACCA),
    onSecondary = Color.White,
    onSurface = Color.Black,
    onBackground = Color.Black,
)

val Color.Companion.FavoriteColor: Color
    get() = Color(0xFFF74545)

@Composable
fun PetAppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    colors: Colors? = null,
    content: @Composable () -> Unit,
) {

    val themeColors = colors ?: if (isDarkTheme) PetAppDarkPalette else PetAppLightPalette

    MaterialTheme(
        colors = themeColors,
        content = content,
        typography = PetTypography,
        shapes = PetAppShapes
    )
}
