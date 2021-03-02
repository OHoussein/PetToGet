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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ohoussein.pettoget.R
import com.ohoussein.pettoget.theme.AppbarFontFamily
import com.ohoussein.pettoget.theme.PetAppTheme

@Composable
fun PetAppScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onBackButton: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    PetAppTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { PetAppTopBar(onBackButton) },

            content = content,
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colors.background),
        )
    }
}

@Composable
fun PetAppTopBar(
    onBackButton: (() -> Unit)?
) {
    TopAppBar(
        elevation = 0.dp,
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = LocalTextStyle.current.copy(fontFamily = AppbarFontFamily),
            )
        },
        navigationIcon = {
            if (onBackButton != null) {
                IconButton(onClick = onBackButton) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pet_placeholder),
                    contentDescription = stringResource(id = R.string.back),
                    modifier = Modifier.padding(12.dp)
                )
            }
        },
        contentColor = MaterialTheme.colors.onPrimary,
        backgroundColor = MaterialTheme.colors.primary,
    )
}
