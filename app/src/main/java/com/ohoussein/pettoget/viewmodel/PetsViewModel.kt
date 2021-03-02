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
package com.ohoussein.pettoget.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.ohoussein.pettoget.data.DataProvider
import com.ohoussein.pettoget.model.ContentState
import com.ohoussein.pettoget.model.Pet
import com.ohoussein.pettoget.model.PetType
import kotlinx.coroutines.delay

class PetsViewModel(app: Application) : AndroidViewModel(app) {

    private val dataProvider: DataProvider by lazy {
        DataProvider(appContext = app)
    }

    private val allPets: LiveData<ContentState<List<Pet>>> = liveData {
        emit(ContentState.Loading())
        delay(1500) // simulate a delay
        val data = dataProvider.getPets()
        emit(ContentState.Success(data))
    }

    private val _filterPetType = MutableLiveData<PetType>(PetType.DOG)
    val filterPetType: LiveData<PetType>
        get() = _filterPetType

    private val _favoritePets = MutableLiveData<List<Pet>>()
    val favoritePets: LiveData<List<Pet>>
        get() = _favoritePets

    val petTypes = listOf(PetType.DOG, PetType.CAT, PetType.OTHER)

    private val _selectedPetId = MutableLiveData<Long>()

    val pets: LiveData<ContentState<List<Pet>>> = MediatorLiveData<ContentState<List<Pet>>>()
        .apply {
            addSource(allPets) { state ->
                value = if (state is ContentState.Success)
                    ContentState.Success(
                        state.data.filter(filterPetType.value)
                            .applyFavorites(favoritePets.value ?: emptyList())
                    )
                else
                    state
            }
            addSource(_filterPetType) {
                allPets.value?.let { state ->
                    value = if (state is ContentState.Success)
                        ContentState.Success(
                            state.data.filter(filterPetType.value)
                                .applyFavorites(favoritePets.value ?: emptyList())
                        )
                    else
                        state
                }
            }
            addSource(_favoritePets) { favorites ->
                allPets.value?.let { state ->
                    value = if (state is ContentState.Success)
                        ContentState.Success(
                            state.data.filter(filterPetType.value).applyFavorites(favorites)
                        )
                    else
                        state
                }
            }
        }

    val selectedPet: LiveData<Pet> = MediatorLiveData<Pet>()
        .apply {
            addSource(pets) {
                _selectedPetId.value?.let { selectedPetId ->
                    val state = pets.value as? ContentState.Success ?: return@addSource
                    value = state.data.getByID(selectedPetId)
                }
            }

            addSource(_selectedPetId) { selectedPetId ->
                val state = pets.value as? ContentState.Success ?: return@addSource
                value = state.data.getByID(selectedPetId)
            }

            addSource(_favoritePets) {
                _selectedPetId.value?.let { selectedPetId ->
                    val state = pets.value as? ContentState.Success ?: return@addSource
                    value = state.data.getByID(selectedPetId)
                }
            }
        }

    fun setSelectedPet(id: Long) {
        _selectedPetId.value = id
    }

    fun setFilterPetType(type: PetType) {
        _filterPetType.value = type
    }

    private fun List<Pet>.getByID(id: Long): Pet? = this.firstOrNull { it.id == id }
        .apply {
            this?.favorite = _favoritePets.value?.any { it.id == this?.id } == true
        }

    private fun List<Pet>.filter(type: PetType?): List<Pet> = this.filter {
        type == null || type == PetType.OTHER || it.type == type
    }

    private fun List<Pet>.applyFavorites(favorites: List<Pet>): List<Pet> {
        return this.map { pet ->
            pet.favorite = favorites.any { it.id == pet.id }
            pet
        }
    }

    fun toggleFavorite(pet: Pet) {
        if (pet.favorite) {
            pet.favorite = false
            _favoritePets.value = _favoritePets.value?.filter { it.id != pet.id }
        } else {
            pet.favorite = true
            _favoritePets.value = (_favoritePets.value?.toMutableList() ?: mutableListOf())
                .apply {
                    add(pet)
                }
        }
    }
}
