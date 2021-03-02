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
package com.ohoussein.pettoget

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.ohoussein.pettoget.model.ContentState
import com.ohoussein.pettoget.model.Pet
import com.ohoussein.pettoget.model.PetType
import com.ohoussein.pettoget.screens.PetDetailsScreen
import com.ohoussein.pettoget.screens.PetListScreen
import com.ohoussein.pettoget.screens.Screens
import com.ohoussein.pettoget.screens.components.PetAppScaffold
import com.ohoussein.pettoget.screens.components.ScreenLoading
import com.ohoussein.pettoget.viewmodel.PetsViewModel

class RootActivity : AppCompatActivity() {

    private val viewModel: PetsViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            val petsListState: ContentState<List<Pet>> by viewModel.pets.observeAsState(ContentState.Loading())
            val selectedPetType: PetType by viewModel.filterPetType.observeAsState(PetType.OTHER)

            NavHost(navController, startDestination = Screens.PETS_LIST) {
                composable(Screens.PETS_LIST) {
                    var petListScreenShown by remember { mutableStateOf(false) }

                    LaunchedEffect(Screens.PETS_LIST) {
                        petListScreenShown = true
                    }

                    AnimatedVisibility(
                        visible = petListScreenShown,
                        initiallyVisible = false,
                        enter = slideInHorizontally(initialOffsetX = { - it / 2 }),
                    ) {

                        PetAppScaffold {
                            AnimatedVisibility(
                                visible = (petsListState is ContentState.Success),
                                enter = slideInVertically(initialOffsetY = { -40 }) +
                                    fadeIn(initialAlpha = 0.3F)
                            ) {
                                PetListScreen(
                                    pets = (petsListState as? ContentState.Success)?.data ?: emptyList(),
                                    petTypes = viewModel.petTypes,
                                    selectedPetType = selectedPetType,
                                    onPetClicked = {
                                        navController.navigate(Screens.PetDetails.path(it.id))
                                    },
                                    onSelectPetType = { viewModel.setFilterPetType(it) },
                                )
                            }

                            AnimatedVisibility(visible = (petsListState is ContentState.Loading)) {
                                ScreenLoading(modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
                composable(
                    Screens.PetDetails.PATH,
                    arguments = listOf(
                        navArgument(Screens.PetDetails.ARG_PET_ID) {
                            type = NavType.LongType
                        }
                    ),
                ) { backStackEntry ->

                    var petDetailsShown by remember { mutableStateOf(false) }

                    LaunchedEffect(id) {
                        petDetailsShown = true
                    }

                    AnimatedVisibility(
                        visible = petDetailsShown,
                        initiallyVisible = false,
                        enter = slideInHorizontally(initialOffsetX = { it / 2 }),
                    ) {

                        PetAppScaffold(
                            onBackButton = {
                                petDetailsShown = false
                                navController.popBackStack()
                            }
                        ) {
                            val selectedPet: Pet? by viewModel.selectedPet.observeAsState()
                            val favorites: List<Pet>? by viewModel.favoritePets.observeAsState()

                            backStackEntry.arguments?.getLong(Screens.PetDetails.ARG_PET_ID)
                                ?.let { id ->
                                    viewModel.setSelectedPet(id)
                                    selectedPet?.let { pet ->
                                        PetDetailsScreen(
                                            pet = pet,
                                            favorites = favorites ?: emptyList(),
                                            onToggleFavorite = { viewModel.toggleFavorite(it) }
                                        )
                                    }
                                } ?: navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
