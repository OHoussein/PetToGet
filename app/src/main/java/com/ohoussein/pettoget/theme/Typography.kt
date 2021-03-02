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

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ohoussein.pettoget.R

private val MontserratFontFamily = FontFamily(
    Font(R.font.montserrat_medium),
    Font(R.font.montserrat_light, FontWeight.Light),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

val PaytoneOneFontFamily = FontFamily(
    Font(R.font.paytone_one_regular),
)

val AppbarFontFamily = PaytoneOneFontFamily

val PetTypography = Typography(
    defaultFontFamily = MontserratFontFamily,
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    )

)
