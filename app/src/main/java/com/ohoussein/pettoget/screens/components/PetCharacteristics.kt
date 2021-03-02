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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.ohoussein.pettoget.model.Level
import com.ohoussein.pettoget.model.Pet
import com.ohoussein.pettoget.model.PetCharacteristic

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PetCharacteristics(
    pet: Pet,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        pet.characteristics.chunked(2)
            .forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.Center) {
                    rowItems.forEach { columnItems ->
                        PetCharacteristicItem(
                            data = columnItems,
                            modifier = Modifier
                                .weight(1F)
                                .padding(12.dp)
                        )
                    }
                }
            }
    }
}

@Composable
internal fun PetCharacteristicItem(data: PetCharacteristic, modifier: Modifier) {
    Card(
        modifier = modifier,
        elevation = 3.dp,
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val progress: Float = (data.level.value / Level.MAX.toFloat()) * 360F
            val strokeColor = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.2F)
            val activeColor = MaterialTheme.colors.secondaryVariant

            var show by remember { mutableStateOf(false) }
            val progressAnimation: Float by animateFloatAsState(if (show) progress else 0F)

            LaunchedEffect(data.label) {
                show = true
            }

            Canvas(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp)
                    .size(38.dp),
                onDraw = {
                    drawArc(
                        color = strokeColor,
                        startAngle = 0F,
                        sweepAngle = 360F,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx()),
                    )
                    drawArc(
                        color = activeColor,
                        startAngle = -90F,
                        sweepAngle = progressAnimation,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx()),
                    )
                }
            )
            Spacer(modifier = Modifier.size(3.dp))

            Text(
                text = data.label,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
