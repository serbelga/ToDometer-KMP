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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import dev.sergiobelda.todometer.common.data.doIfSuccess
import dev.sergiobelda.todometer.common.model.Task
import dev.sergiobelda.todometer.common.model.TaskState
import dev.sergiobelda.todometer.wear.R
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TaskListTasksScreen(
    taskListId: String,
    addTask: (String) -> Unit,
    taskListTasksViewModel: TaskListTasksViewModel = getViewModel(
        parameters = { parametersOf(taskListId) }
    )
) {
    val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()
    val tasksResultState = taskListTasksViewModel.tasks.collectAsState()
    Scaffold(
        positionIndicator = { PositionIndicator(scalingLazyListState = scalingLazyListState) }
    ) {
        ScalingLazyColumn(
            contentPadding = PaddingValues(
                top = 28.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 40.dp
            ),
            state = scalingLazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                AddTaskButton {
                    addTask(taskListId)
                }
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            tasksResultState.value.doIfSuccess { tasks ->
                if (tasks.isNullOrEmpty()) {
                    item {
                        Text(text = stringResource(id = R.string.no_tasks))
                    }
                } else {
                    items(tasks) { task ->
                        TaskItem(
                            task,
                            onDoingClick = { taskListTasksViewModel.setTaskDoing(task.id) },
                            onDoneClick = { taskListTasksViewModel.setTaskDone(task.id) },
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onDoingClick: (String) -> Unit,
    onDoneClick: (String) -> Unit,
    onClick: (String) -> Unit
) {
    // Use SplitToggleChip if onClick is needed.
    ToggleChip(
        // colors = ChipDefaults.secondaryChipColors(),
        checked = task.state == TaskState.DONE,
        onCheckedChange = {
            if (task.state == TaskState.DOING) {
                onDoneClick(task.id)
            } else {
                onDoingClick(task.id)
            }
        },
        label = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.onSurface,
                text = task.title
            )
        }
    )
}

@Composable
fun AddTaskButton(onClick: () -> Unit) {
    Chip(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        colors = ChipDefaults.secondaryChipColors(),
        icon = {
            Icon(
                Icons.Rounded.Add,
                contentDescription = "Add"
            )
        },
        label = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Add Task"
            )
        },
        onClick = onClick
    )
}
