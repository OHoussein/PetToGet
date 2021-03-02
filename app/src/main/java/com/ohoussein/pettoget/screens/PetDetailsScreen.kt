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
package com.ohoussein.pettoget.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.request.RequestOptions
import com.ohoussein.pettoget.R
import com.ohoussein.pettoget.model.Gender
import com.ohoussein.pettoget.model.Pet
import com.ohoussein.pettoget.model.PetOwner
import com.ohoussein.pettoget.screens.components.DurationString
import com.ohoussein.pettoget.screens.components.Pager
import com.ohoussein.pettoget.screens.components.PagerIndicator
import com.ohoussein.pettoget.screens.components.PagerState
import com.ohoussein.pettoget.screens.components.PetCharacteristics
import com.ohoussein.pettoget.screens.components.PetImage
import com.ohoussein.pettoget.util.log
import dev.chrisbanes.accompanist.glide.GlideImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import dev.chrisbanes.accompanist.imageloading.MaterialLoadingImage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PetDetailsScreen(
    pet: Pet,
    @Suppress("UNUSED_PARAMETER") favorites: List<Pet>,
    onToggleFavorite: (Pet) -> Unit,
) {
    val state = rememberScrollState()

    Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.fillMaxSize()) {

        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.verticalScroll(state)
            ) {
                TopScreen(pet)
                PetOwner(
                    pet.owner,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 12.dp)
                )
                PetAdoptionDescription(
                    description = pet.description,
                    modifier = Modifier.padding(8.dp)
                )

                Divider(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Text(
                    text = stringResource(id = R.string.characteristics),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                PetCharacteristics(pet = pet, modifier = Modifier.padding(8.dp))
                Spacer(modifier = Modifier.padding(vertical = 44.dp))
            }
        }
        BottomButtons(
            pet = pet,
            onToggleFavorite = onToggleFavorite,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )
    }
}

@Composable
fun TopScreen(pet: Pet) {

    Box(modifier = Modifier.fillMaxSize()) {

        ConstraintLayout {
            val (image, basicDetails, indicators) = createRefs()

            var currentPage: Int by remember { mutableStateOf(0) }
            val pagerState: PagerState = remember { PagerState(currentPage = currentPage) }
            pagerState.maxPage = (pet.images.size - 1).coerceAtLeast(0)

            pagerState.onPageChanged = { currentPage = it }

            LocalConfiguration.current.screenWidthDp
            val imgHeight = petDetailImageHeight()

            PetImagesPage(
                images = pet.images,
                pagerState = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.value(imgHeight)
                    }
            )

            PagerIndicator(
                selectedPage = currentPage,
                countPages = pagerState.maxPage,

                modifier = Modifier
                    .constrainAs(indicators) {
                        bottom.linkTo(basicDetails.top, margin = 8.dp)
                        centerHorizontallyTo(parent)
                    },
                indicatorSize = 9.dp,
            )

            var show by remember { mutableStateOf(false) }
            val alpha: Float by animateFloatAsState(if (show) 1F else 0F)

            LaunchedEffect("TopScreen") {
                show = true
            }

            PetBasicDetails(
                pet,
                modifier = Modifier
                    .padding(horizontal = 24.dp) // , vertical = paddingVertical.dp)
                    .graphicsLayer(alpha = alpha)
                    .constrainAs(basicDetails) {
                        centerAround(image.bottom)
                        centerHorizontallyTo(image)
                    }
            )
        }
    }
}

@Composable
fun PetImagesPage(
    images: List<String>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = remember { PagerState() },
) {
    Pager(
        state = pagerState,
        modifier = modifier
    ) {

        val image = images[page]
        PetImage(
            imageUrl = image,
            modifier = Modifier.fillMaxSize(),
            contentDescription = null
        )
    }
}

@Composable
fun PetBasicDetails(pet: Pet, modifier: Modifier = Modifier) {
    Card(
        elevation = 8.dp,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(12.dp)
        ) {
            // Name and breed
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onPrimary,
                )
                Spacer(modifier = Modifier.size(6.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.onPrimary)
                )
                Spacer(modifier = Modifier.size(6.dp))

                Text(
                    text = pet.breed,
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            // Address
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = stringResource(id = R.string.address),
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = pet.location.address,
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onPrimary,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(4.dp)
            ) {

                PetInfoItem(
                    label = stringResource(id = R.string.age),
                    value = DurationString(durationInDays = pet.ageInDays),
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                PetInfoItem(
                    label = stringResource(id = R.string.gender),
                    value = when (pet.gender) {
                        Gender.FEMALE -> stringResource(id = R.string.gender_female)
                        Gender.MALE -> stringResource(id = R.string.gender_male)
                    },
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                pet.weightKg?.let { weightKg ->
                    PetInfoItem(
                        label = stringResource(id = R.string.weight),
                        value = stringResource(id = R.string.weight_kg, weightKg),
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun PetInfoItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onPrimary,
            modifier = modifier.padding(2.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onPrimary,
            modifier = modifier.padding(2.dp)
        )
    }
}

@Composable
fun PetOwner(owner: PetOwner, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        GlideImage(
            data = owner.image,
            requestBuilder = {
                val options = RequestOptions()
                options.circleCrop()
                apply(options)
            },
            modifier = Modifier
                .size(64.dp)
        ) { imageState ->
            when (imageState) {
                is ImageLoadState.Success -> {
                    MaterialLoadingImage(
                        result = imageState,
                        fadeInEnabled = true,
                        contentDescription = owner.fullDisplayName,
                        contentScale = ContentScale.Crop,
                    )
                }
                else -> {
                    CircularProgressIndicator(
                        Modifier
                            .padding(18.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
        Text(
            text = owner.fullDisplayName,
            style = MaterialTheme.typography.body1,
            modifier = modifier.padding(4.dp)
        )
    }
}

@Composable
fun PetAdoptionDescription(description: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.FormatQuote,
            contentDescription = stringResource(id = R.string.address),
            tint = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = description,
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
        )
    }
}

@Composable
fun BottomButtons(
    pet: Pet,
    onToggleFavorite: (Pet) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier.height(48.dp)) {
        Card(
            backgroundColor = MaterialTheme.colors.secondary,
            shape = MaterialTheme.shapes.small,
            elevation = 8.dp,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable { onToggleFavorite(pet) }
        ) {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight()
            ) {
                Icon(
                    // modifier = Modifier.fillMaxSize(),
                    imageVector = if (pet.favorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(id = R.string.favorite),
                    tint = MaterialTheme.colors.onSecondary,
                )
            }
        }

        Card(
            backgroundColor = MaterialTheme.colors.secondary,
            shape = MaterialTheme.shapes.small,
            elevation = 8.dp,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable {
                    log("Adoption")
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    // modifier = Modifier.fillMaxSize(),
                    text = stringResource(id = R.string.adoption),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.onSecondary,
                )
            }
        }
    }
}
