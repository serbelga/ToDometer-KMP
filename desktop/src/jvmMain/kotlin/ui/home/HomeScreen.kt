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

package ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MenuOpen
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import dev.sergiobelda.todometer.common.compose.ui.components.task.TaskItem
import dev.sergiobelda.todometer.common.compose.ui.components.tasklist.TaskListItem
import dev.sergiobelda.todometer.common.compose.ui.components.tasklist.TaskListProgress
import dev.sergiobelda.todometer.common.compose.ui.components.title.ToDometerTitle
import dev.sergiobelda.todometer.common.compose.ui.designsystem.components.HorizontalDivider
import dev.sergiobelda.todometer.common.domain.doIfError
import dev.sergiobelda.todometer.common.domain.doIfSuccess
import dev.sergiobelda.todometer.common.domain.model.TaskItem
import dev.sergiobelda.todometer.common.domain.model.TaskList
import dev.sergiobelda.todometer.common.domain.usecase.task.DeleteTaskUseCase
import dev.sergiobelda.todometer.common.domain.usecase.task.GetTaskListSelectedTasksUseCase
import dev.sergiobelda.todometer.common.domain.usecase.task.InsertTaskInTaskListSelectedUseCase
import dev.sergiobelda.todometer.common.domain.usecase.task.SetTaskDoingUseCase
import dev.sergiobelda.todometer.common.domain.usecase.task.SetTaskDoneUseCase
import dev.sergiobelda.todometer.common.domain.usecase.tasklist.GetTaskListSelectedUseCase
import dev.sergiobelda.todometer.common.domain.usecase.tasklist.GetTaskListsUseCase
import dev.sergiobelda.todometer.common.domain.usecase.tasklist.InsertTaskListUseCase
import dev.sergiobelda.todometer.common.domain.usecase.tasklist.SetTaskListSelectedUseCase
import dev.sergiobelda.todometer.common.resources.MR
import koin
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    navigateToTaskDetail: () -> Unit
) {
    var addTaskListAlertDialogState by remember { mutableStateOf(false) }
    var addTaskAlertDialogState by remember { mutableStateOf(false) }
    var deleteTaskAlertDialogState by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf("") }

    val setTaskDoingUseCase = koin.get<SetTaskDoingUseCase>()
    val setTaskDoneUseCase = koin.get<SetTaskDoneUseCase>()
    val getTaskListSelectedUseCase = koin.get<GetTaskListSelectedUseCase>()
    val setTaskListSelectedUseCase = koin.get<SetTaskListSelectedUseCase>()
    val getTaskListSelectedTasksUseCase = koin.get<GetTaskListSelectedTasksUseCase>()
    val getTaskListsUseCase = koin.get<GetTaskListsUseCase>()
    val insertTaskListUseCase = koin.get<InsertTaskListUseCase>()
    val insertTaskInTaskListSelectedUseCase = koin.get<InsertTaskInTaskListSelectedUseCase>()
    val deleteTaskUseCase = koin.get<DeleteTaskUseCase>()

    var taskListSelected: TaskList? by remember { mutableStateOf(null) }
    val taskListResultState by getTaskListSelectedUseCase().collectAsState(null)
    taskListResultState?.doIfSuccess { taskListSelected = it }
        ?.doIfError { taskListSelected = null }

    var tasks: List<TaskItem> by remember { mutableStateOf(emptyList()) }
    val tasksResultState by getTaskListSelectedTasksUseCase().collectAsState(null)
    tasksResultState?.doIfSuccess { tasks = it }

    var taskLists: List<TaskList> by remember { mutableStateOf(emptyList()) }
    val taskListsResultState by getTaskListsUseCase().collectAsState(null)
    taskListsResultState?.doIfSuccess { taskLists = it }

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val closeDrawer: suspend () -> Unit = {
        drawerState.close()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                TaskListsNavigationDrawer(
                    taskLists,
                    taskListSelected?.id ?: "",
                    stringResource(resource = MR.strings.default_task_list_name),
                    onTaskListClick = {
                        coroutineScope.launch {
                            setTaskListSelectedUseCase.invoke(it)
                        }
                    },
                    onAddTaskListClick = { addTaskListAlertDialogState = true },
                    onMenuCloseClick = { coroutineScope.launch { closeDrawer() } }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            if (drawerState.isOpen) {
                                Icon(Icons.Rounded.MenuOpen, contentDescription = null)
                            } else {
                                Icon(Icons.Rounded.Menu, contentDescription = null)
                            }
                        }
                    },
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            ToDometerTitle()
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Outlined.MoreVert, contentDescription = "Settings")
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    icon = {
                        Icon(Icons.Rounded.Add, stringResource(resource = MR.strings.add_task))
                    },
                    text = {
                        Text(stringResource(resource = MR.strings.add_task))
                    },
                    onClick = { addTaskAlertDialogState = true }
                )
            },
            floatingActionButtonPosition = FabPosition.End
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                if (addTaskListAlertDialogState) {
                    AddTaskListAlertDialog(
                        onDismissRequest = { addTaskListAlertDialogState = false }
                    ) { taskListName ->
                        coroutineScope.launch {
                            insertTaskListUseCase.invoke(taskListName)
                        }
                    }
                }
                if (addTaskAlertDialogState) {
                    AddTaskAlertDialog(
                        onDismissRequest = { addTaskAlertDialogState = false }
                    ) { title, description, tag ->
                        coroutineScope.launch {
                            insertTaskInTaskListSelectedUseCase(title, tag, description)
                        }
                    }
                }
                if (deleteTaskAlertDialogState) {
                    DeleteTaskAlertDialog(
                        onDismissRequest = {
                            coroutineScope.launch {
                                selectedTask = ""
                                deleteTaskAlertDialogState = false
                            }
                        }
                    ) {
                        coroutineScope.launch {
                            deleteTaskUseCase(selectedTask)
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(paddingValues)) {
                TaskListProgress(
                    taskListSelected?.name
                        ?: stringResource(resource = MR.strings.default_task_list_name),
                    tasks,
                    modifier = Modifier.padding(top = 16.dp)
                )
                if (tasks.isEmpty()) {
                    EmptyTasksListView()
                } else {
                    TasksListView(
                        tasks,
                        onDoingClick = {
                            coroutineScope.launch {
                                setTaskDoingUseCase(it)
                            }
                        },
                        onDoneClick = {
                            coroutineScope.launch {
                                setTaskDoneUseCase(it)
                            }
                        },
                        onTaskItemClick = {
                            navigateToTaskDetail()
                        },
                        onTaskItemLongClick = {
                            deleteTaskAlertDialogState = true
                            selectedTask = it
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TasksListView(
    tasks: List<TaskItem>,
    onDoingClick: (String) -> Unit,
    onDoneClick: (String) -> Unit,
    onTaskItemClick: (String) -> Unit,
    onTaskItemLongClick: (String) -> Unit
) {
    LazyColumn {
        items(tasks, key = { it.id }) {
            TaskItem(
                taskItem = it,
                onDoingClick = onDoingClick,
                onDoneClick = onDoneClick,
                onClick = onTaskItemClick,
                onLongClick = onTaskItemLongClick,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@Composable
private fun EmptyTasksListView() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(bottom = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource("images/no_tasks.svg"),
                modifier = Modifier.size(240.dp).padding(bottom = 24.dp),
                contentDescription = null
            )
            Text(stringResource(resource = MR.strings.no_tasks))
        }
    }
}

@Composable
private fun TaskListsNavigationDrawer(
    taskLists: List<TaskList>,
    selectedTaskListId: String,
    defaultTaskListName: String,
    onTaskListClick: (taskListId: String) -> Unit,
    onAddTaskListClick: () -> Unit,
    onMenuCloseClick: () -> Unit
) {
    Row(
        modifier = Modifier.height(72.dp).fillMaxWidth().padding(start = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuCloseClick) {
            Icon(Icons.Rounded.MenuOpen, contentDescription = null)
        }
        ToDometerTitle(modifier = Modifier.padding(start = 4.dp))
    }
    HorizontalDivider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(56.dp)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(resource = MR.strings.task_lists),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onAddTaskListClick) {
            Text(text = stringResource(resource = MR.strings.add_task_list))
        }
    }
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        item {
            TaskListItem(defaultTaskListName, selectedTaskListId == "") {
                onTaskListClick("")
            }
        }
        items(taskLists) { taskList ->
            TaskListItem(
                taskList.name,
                taskList.id == selectedTaskListId
            ) {
                onTaskListClick(taskList.id)
            }
        }
    }
}
