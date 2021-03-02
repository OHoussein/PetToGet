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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ohoussein.pettoget.R
import dev.chrisbanes.accompanist.glide.GlideImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import dev.chrisbanes.accompanist.imageloading.MaterialLoadingImage

@Composable
fun PetImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String?,
    placeholderModifier: Modifier = Modifier
        .wrapContentSize()
        .size(64.dp),
) {
    GlideImage(
        data = imageUrl,
        modifier = modifier,
    ) { imageState ->
        when (imageState) {
            is ImageLoadState.Success -> {
                MaterialLoadingImage(
                    result = imageState,
                    fadeInEnabled = true,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                )
            }
            else -> {
                Image(
                    painter = painterResource(id = R.drawable.ic_pet_placeholder),
                    contentDescription = null,
                    modifier = placeholderModifier
                )
            }
        }
    }
}

@Composable
fun PetImageZoomable(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String?,
    placeholderModifier: Modifier = Modifier
        .wrapContentSize()
        .size(64.dp),
) {
    Box(
        modifier = modifier
            .clip(RectangleShape)
            .fillMaxSize()
            .background(Color.Black)
        //  .zoomable(onZoomDelta = { scale.value *= it })
    ) {
        GlideImage(
            data = imageUrl,
            modifier = Modifier.fillMaxSize(),
        ) { imageState ->
            when (imageState) {
                is ImageLoadState.Success -> {
                    MaterialLoadingImage(
                        result = imageState,
                        fadeInEnabled = false,
                        contentDescription = contentDescription,
                        contentScale = ContentScale.FillBounds,
                    )
                }
                else -> {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pet_placeholder),
                        contentDescription = null,
                        modifier = placeholderModifier

                    )
                }
            }
        }
    }
}
