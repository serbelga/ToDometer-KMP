/*
 * Copyright 2021 Sergio Belda
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

package dev.sergiobelda.todometer.wear.ui.tasklisttasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.sergiobelda.todometer.common.domain.doIfError
import dev.sergiobelda.todometer.common.domain.doIfSuccess
import dev.sergiobelda.todometer.common.domain.usecase.GetTaskListTasksUseCase
import dev.sergiobelda.todometer.common.domain.usecase.GetTaskListUseCase
import dev.sergiobelda.todometer.common.domain.usecase.InsertTaskUseCase
import dev.sergiobelda.todometer.common.domain.usecase.SetTaskDoingUseCase
import dev.sergiobelda.todometer.common.domain.usecase.SetTaskDoneUseCase
import dev.sergiobelda.todometer.common.domain.usecase.UpdateTaskListNameUseCase
import kotlinx.coroutines.launch

class TaskListTasksViewModel(
    private val taskListId: String,
    private val getTaskListTasksUseCase: GetTaskListTasksUseCase,
    private val getTaskListUseCase: GetTaskListUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val setTaskDoingUseCase: SetTaskDoingUseCase,
    private val setTaskDoneUseCase: SetTaskDoneUseCase,
    private val updateTaskListNameUseCase: UpdateTaskListNameUseCase
) : ViewModel() {

    var taskListTasksUiState by mutableStateOf(
        TaskListTasksUiState(
            isLoadingTaskList = true,
            isLoadingTasks = true
        )
    )
        private set

    init {
        getTaskList()
        getTaskListTasks()
    }

    private fun getTaskList() = viewModelScope.launch {
        getTaskListUseCase(taskListId).collect { result ->
            result.doIfSuccess { taskList ->
                taskListTasksUiState = taskListTasksUiState.copy(
                    isLoadingTaskList = false,
                    taskList = taskList,
                    isDefaultTaskList = false
                )
            }.doIfError {
                taskListTasksUiState = taskListTasksUiState.copy(
                    isLoadingTaskList = false,
                    taskList = null,
                    isDefaultTaskList = true
                )
            }
        }
    }

    private fun getTaskListTasks() = viewModelScope.launch {
        getTaskListTasksUseCase(taskListId).collect { result ->
            result.doIfSuccess { tasks ->
                taskListTasksUiState = taskListTasksUiState.copy(
                    isLoadingTasks = false,
                    tasks = tasks
                )
            }.doIfError {
                taskListTasksUiState = taskListTasksUiState.copy(
                    isLoadingTasks = false,
                    tasks = emptyList()
                )
            }
        }
    }

    fun insertTask(title: String) = viewModelScope.launch {
        insertTaskUseCase.invoke(taskListId, title)
    }

    fun setTaskDoing(id: String) = viewModelScope.launch {
        setTaskDoingUseCase(id)
    }

    fun setTaskDone(id: String) = viewModelScope.launch {
        setTaskDoneUseCase(id)
    }

    fun updateTaskListName(name: String) = viewModelScope.launch {
        updateTaskListNameUseCase(taskListId, name)
    }
}
