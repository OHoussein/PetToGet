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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ohoussein.pettoget.R
import com.ohoussein.pettoget.model.Gender
import com.ohoussein.pettoget.model.Pet
import com.ohoussein.pettoget.model.PetType
import com.ohoussein.pettoget.screens.components.DurationString
import com.ohoussein.pettoget.screens.components.PetImage
import com.ohoussein.pettoget.theme.FavoriteColor

private val infoIconSize = 14.dp
private val cellFilterSize = 50.dp

@Composable
fun PetListScreen(
    pets: List<Pet>,
    petTypes: List<PetType>,
    selectedPetType: PetType,
    onPetClicked: (Pet) -> Unit,
    onSelectPetType: (PetType) -> Unit,
    modifier: Modifier = Modifier,
) {
    PetList(
        petList = pets,
        petTypes = petTypes,
        selectedPetType = selectedPetType,
        onPetClicked = onPetClicked,
        onSelectPetType = onSelectPetType,
        modifier = modifier,
    )
}

@Composable
fun PetFilter(
    petTypes: List<PetType>,
    selectedPetType: PetType,
    onSelectPetType: (PetType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = 64.dp, bottomEnd = 64.dp))
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 12.dp),

        horizontalArrangement = Arrangement.Center
    ) {

        petTypes.forEach { petType ->
            PetType(
                type = petType,
                selected = selectedPetType == petType,
                onSelectPetType = onSelectPetType
            )
            Spacer(modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun PetType(
    type: PetType,
    selected: Boolean,
    onSelectPetType: (PetType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        val label = when (type) {
            PetType.CAT -> stringResource(id = R.string.cat)
            PetType.DOG -> stringResource(id = R.string.dog)
            PetType.OTHER -> stringResource(id = R.string.other)
        }
        val borderColor by animateColorAsState(if (selected) MaterialTheme.colors.onPrimary else Color.Transparent)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(BorderStroke(0.7.dp, borderColor), shape = MaterialTheme.shapes.small)
                .clickable {
                    onSelectPetType(type)
                }
        ) {

            val tint = MaterialTheme.colors.onPrimary
            val iconModifier = Modifier
                .size(cellFilterSize)
                .padding(12.dp)

            when (type) {
                PetType.CAT -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cat),
                        contentDescription = label,
                        tint = tint,
                        modifier = iconModifier,
                    )
                }
                PetType.DOG -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dog),
                        contentDescription = label,
                        tint = tint,
                        modifier = iconModifier,
                    )
                }
                PetType.OTHER -> {
                    Icon(
                        imageVector = Icons.Outlined.Pets,
                        contentDescription = label,
                        tint = tint,
                        modifier = iconModifier,
                    )
                }
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PetList(
    petList: List<Pet>,
    petTypes: List<PetType>,
    selectedPetType: PetType,
    onPetClicked: (Pet) -> Unit,
    onSelectPetType: (PetType) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(petList.size) { index ->
            if (index == 0) {
                PetFilter(
                    petTypes = petTypes,
                    selectedPetType = selectedPetType,
                    onSelectPetType = onSelectPetType,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )
            } else {
                val realIndex = index - 1
                PetListItem(
                    pet = petList[realIndex],
                    onPetClicked = onPetClicked,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PetListItem(
    pet: Pet,
    onPetClicked: (Pet) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {

        Card(
            elevation = 8.dp,
            modifier = modifier
                .clickable {
                    onPetClicked(pet)
                }
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 178.dp)
                        .background(MaterialTheme.colors.primary)
                        .fillMaxSize()
                ) {
                    PetInfoLayout(pet)
                }
            }
        }
        Box(contentAlignment = Alignment.TopEnd) {
            PetImage(
                imageUrl = pet.images.first(),
                modifier = Modifier
                    .width(170.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(42.dp)),
                contentDescription = null,
            )
            if (pet.favorite) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = stringResource(id = R.string.favorite),
                    tint = Color.FavoriteColor,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun PetInfoLayout(pet: Pet) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {

            Text(
                text = pet.name,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onPrimary,
            )

            val genderImageContentDesc: Pair<String, ImageVector> = when (pet.gender) {
                Gender.FEMALE -> Pair(
                    stringResource(id = R.string.gender_female),
                    Icons.Outlined.Female,
                )
                Gender.MALE -> Pair(
                    stringResource(id = R.string.gender_male),
                    Icons.Outlined.Male,
                )
            }

            Icon(
                imageVector = genderImageContentDesc.second,
                contentDescription = genderImageContentDesc.first,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(28.dp),
            )
        }

        Text(
            text = pet.breed,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = stringResource(id = R.string.age),
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(infoIconSize),
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = DurationString(pet.ageInDays),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onPrimary,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Place,
                contentDescription = stringResource(id = R.string.address),
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(infoIconSize),
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = pet.location.address,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onPrimary,
            )
        }
    }
}
