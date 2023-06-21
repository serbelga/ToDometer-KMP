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

package dev.sergiobelda.todometer.desktop.ui.addtask

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.sergiobelda.todometer.common.compose.ui.addtask.AddTaskScreen
import dev.sergiobelda.todometer.common.compose.ui.addtask.AddTaskViewModel
import dev.sergiobelda.todometer.desktop.koin

@Composable
internal fun AddTaskRoute(
    navigateBack: () -> Unit
) {
    val addTaskViewModel: AddTaskViewModel = remember { koin.get() }
    AddTaskScreen(
        navigateBack = navigateBack,
        insertTask = { taskTitle, selectedTag, taskDescription, taskDueDate, taskChecklistItems ->
            addTaskViewModel.insertTask(
                taskTitle,
                selectedTag,
                taskDescription,
                taskDueDate,
                taskChecklistItems
            )
        },
        addTaskUiState = addTaskViewModel.addTaskUiState
    )
}
