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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun PagerIndicator(
    selectedPage: Int,
    countPages: Int,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    indicatorSize: Dp = 3.dp,
    distanceBetween: Dp = 4.dp,
    indicatorColor: Color = Color.White,
    selectedIndicatorColor: Color = MaterialTheme.colors.secondaryVariant,
) {
    val selectedDotId = "selectedDotId"
    Layout(
        modifier = modifier.height(indicatorSize),
        content = {
            repeat(countPages + 1) { index ->
                Indicator(
                    modifier = Modifier.layoutId(index),
                    // color = if (index == selectedPage) selectedIndicatorColor else indicatorColor,
                    color = indicatorColor,
                    shape = shape,
                    size = indicatorSize,
                    distanceBetween = if (index == countPages) 0.dp else distanceBetween,
                )
            }
            Indicator(
                modifier = Modifier.layoutId(selectedDotId),
                color = selectedIndicatorColor,
                shape = shape,
                size = indicatorSize,
                distanceBetween = 0.dp,
                fill = true,
            )
        }
    ) { measurables, constraints ->
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(constraints.maxWidth, constraints.maxHeight) {
            var offset = 0F
            var selectedOffset = 0

            measurables
                .dropLastWhile { it.layoutId == selectedDotId }
                .forEach { measurable ->
                    val placeable = measurable.measure(childConstraints)

                    val xCenterOffset = (constraints.maxWidth - placeable.width) / 2
                    val yCenterOffset = (constraints.maxHeight - placeable.height) / 2

                    val xItemOffset = offset.roundToInt()
                    offset += placeable.width
                    placeable.place(
                        x = xCenterOffset + xItemOffset,
                        y = yCenterOffset
                    )
                    if (measurable.layoutId == selectedPage)
                        selectedOffset = xItemOffset
                }

            val selectedDot = measurables
                .first { it.layoutId == selectedDotId }
            val placeable = selectedDot.measure(childConstraints)
            val xCenterOffset = (constraints.maxWidth - placeable.width) / 2
            val yCenterOffset = (constraints.maxHeight - placeable.height) / 2
            placeable.place(
                x = xCenterOffset + selectedOffset,
                y = yCenterOffset
            )
        }
    }
}

@Composable
fun Indicator(
    color: Color,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    size: Dp = 4.dp,
    distanceBetween: Dp = 4.dp,
    fill: Boolean = false,
) {
    if (fill) {
        Box(
            modifier = modifier
                .size(size)
                .clip(shape)
                .background(color)
        )
    } else {
        Box(
            modifier = modifier
                .size(size)
                .clip(shape)
                .border(BorderStroke(0.7.dp, color), shape = shape)
        )
    }
    if (distanceBetween.value > 0)
        Spacer(modifier = Modifier.size(distanceBetween))
}
