/*
 * Copyright 2023 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.sergiobelda.todometer.common.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.sergiobelda.todometer.common.designsystem.resources.TodometerIcons
import dev.sergiobelda.todometer.common.resources.MR
import dev.sergiobelda.todometer.common.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveActionTopAppBar(
    navigateBack: () -> Unit,
    title: String,
    isSaveButtonEnabled: Boolean,
    onSaveButtonClick: () -> Unit,
    saveButtonTintColor: Color = MaterialTheme.colorScheme.primary
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    TodometerIcons.NavigateBefore,
                    contentDescription = stringResource(MR.strings.back)
                )
            }
        },
        title = { Text(title) },
        actions = {
            TextButton(
                enabled = isSaveButtonEnabled,
                onClick = onSaveButtonClick
            ) {
                Icon(
                    TodometerIcons.Check,
                    contentDescription = stringResource(MR.strings.save),
                    tint = saveButtonTintColor
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = stringResource(MR.strings.save),
                    style = MaterialTheme.typography.bodyMedium,
                    color = saveButtonTintColor
                )
            }
        }
    )
}
