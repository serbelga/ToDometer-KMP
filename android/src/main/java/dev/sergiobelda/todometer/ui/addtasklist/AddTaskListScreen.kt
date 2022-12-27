/*
 * Copyright 2022 Sergio Belda
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

package dev.sergiobelda.todometer.ui.addtasklist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import dev.sergiobelda.todometer.common.compose.ui.addtasklist.AddTaskListContent
import dev.sergiobelda.todometer.common.compose.ui.addtasklist.AddTaskListTopBar
import dev.sergiobelda.todometer.common.compose.ui.designsystem.theme.ToDometerTheme
import dev.sergiobelda.todometer.glance.ToDometerWidgetReceiver
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddTaskListScreen(
    navigateBack: () -> Unit,
    addTaskListViewModel: AddTaskListViewModel = getViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var taskListName by rememberSaveable { mutableStateOf("") }
    var taskListNameInputError by remember { mutableStateOf(false) }

    val addTaskListUiState = addTaskListViewModel.addTaskListUiState
    if (addTaskListUiState.isAdded) {
        navigateBack()
    }

    if (addTaskListUiState.errorUi != null) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = addTaskListUiState.errorUi.message ?: ""
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AddTaskListTopBar(
                navigateBack = navigateBack,
                isSaveButtonEnabled = !addTaskListUiState.isAddingTaskList,
                onSaveButtonClick = {
                    if (taskListName.isBlank()) {
                        taskListNameInputError = true
                    } else {
                        addTaskListViewModel.insertTaskList(taskListName)
                        ToDometerWidgetReceiver().updateData()
                    }
                },
                saveButtonTintColor = if (addTaskListUiState.isAddingTaskList) ToDometerTheme.toDometerColors.onSurfaceMediumEmphasis else MaterialTheme.colorScheme.primary
            )
        },
        content = { paddingValues ->
            AddTaskListContent(
                paddingValues = paddingValues,
                showProgress = addTaskListUiState.isAddingTaskList,
                taskListNameValue = taskListName,
                taskListNameInputError = taskListNameInputError,
                onTaskListNameValueChange = {
                    taskListName = it
                    taskListNameInputError = false
                }
            )
        }
    )
}
