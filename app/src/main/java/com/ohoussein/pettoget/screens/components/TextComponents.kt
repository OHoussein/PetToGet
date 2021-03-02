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
package com.ohoussein.pettoget.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ohoussein.pettoget.R
import kotlin.math.roundToInt

@Composable
fun DurationString(
    durationInDays: Int
): String {
    return when (durationInDays) {
        in 0..14 -> {
            // Days
            stringResource(id = R.string.duration_days_short, durationInDays)
        }
        in 15..70 -> {
            // Weeks
            stringResource(id = R.string.duration_weeks_short, durationInDays / 7)
        }
        in 71..400 -> {
            // Months
            stringResource(
                id = R.string.duration_months_short,
                (durationInDays / 30.417).roundToInt()
            )
        }
        else -> {
            val years = durationInDays / 365
            val months = ((durationInDays % 365) / 30.417).roundToInt()
            if (months > 0)
                stringResource(id = R.string.duration_years_and_months_short, years, months)
            else
                stringResource(id = R.string.duration_years_short, years)
        }
    }
}
