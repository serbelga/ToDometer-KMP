/*
 * Copyright 2024 Sergio Belda
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

package dev.sergiobelda.todometer.app.navhost

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.sergiobelda.navigation.compose.extended.NavAction
import dev.sergiobelda.navigation.compose.extended.composable
import dev.sergiobelda.todometer.app.feature.about.ui.AboutNavDestination
import dev.sergiobelda.todometer.app.feature.addtask.ui.AddTaskNavDestination
import dev.sergiobelda.todometer.app.feature.addtask.ui.AddTaskScreen
import dev.sergiobelda.todometer.app.feature.addtasklist.ui.AddTaskListNavDestination
import dev.sergiobelda.todometer.app.feature.addtasklist.ui.AddTaskListScreen
import dev.sergiobelda.todometer.app.feature.edittask.ui.EditTaskNavDestination
import dev.sergiobelda.todometer.app.feature.edittask.ui.EditTaskSafeNavArgs
import dev.sergiobelda.todometer.app.feature.edittask.ui.EditTaskScreen
import dev.sergiobelda.todometer.app.feature.edittasklist.ui.EditTaskListNavDestination
import dev.sergiobelda.todometer.app.feature.edittasklist.ui.EditTaskListScreen
import dev.sergiobelda.todometer.app.feature.home.ui.HomeNavDestination
import dev.sergiobelda.todometer.app.feature.home.ui.HomeScreen
import dev.sergiobelda.todometer.app.feature.settings.ui.SettingsNavDestination
import dev.sergiobelda.todometer.app.feature.settings.ui.SettingsScreen
import dev.sergiobelda.todometer.app.feature.taskdetails.ui.TaskDetailsNavDestination
import dev.sergiobelda.todometer.app.feature.taskdetails.ui.TaskDetailsSafeNavArgs
import dev.sergiobelda.todometer.app.feature.taskdetails.ui.TaskDetailsScreen
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun TodometerNavHost(
    navController: NavHostController,
    navAction: NavAction,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val navigateBackAction: () -> Unit = {
        keyboardController?.hide()
        navAction.navigateUp()
    }
    NavHost(
        navController = navController,
        startDestination = HomeNavDestination.route,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        homeNode(
            navigateToAddTaskList = { navAction.navigate(AddTaskListNavDestination.safeNavRoute()) },
            navigateToEditTaskList = { navAction.navigate(EditTaskListNavDestination.safeNavRoute()) },
            navigateToAddTask = { navAction.navigate(AddTaskNavDestination.safeNavRoute()) },
            navigateToTaskDetails = { taskId ->
                navAction.navigate(
                    TaskDetailsNavDestination.safeNavRoute(taskId)
                )
            },
            navigateToSettings = { navAction.navigate(SettingsNavDestination.safeNavRoute()) },
            navigateToAbout = { navAction.navigate(AboutNavDestination.safeNavRoute()) }
        )
        taskDetailsNode(
            navigateBack = navigateBackAction,
            navigateToEditTask = { taskId ->
                navAction.navigate(
                    EditTaskNavDestination.safeNavRoute(taskId)
                )
            }
        )
        addTaskListRoute(navigateBack = navigateBackAction)
        editTaskListNode(navigateBack = navigateBackAction)
        addTaskNode(navigateBack = navigateBackAction)
        editTaskNode(navigateBack = navigateBackAction)
        settingsNode(navigateBack = navigateBackAction)
        aboutNode(navigateBack = navigateBackAction)
    }
}

private fun NavGraphBuilder.homeNode(
    navigateToAddTaskList: () -> Unit,
    navigateToEditTaskList: () -> Unit,
    navigateToAddTask: () -> Unit,
    navigateToTaskDetails: (String) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToAbout: () -> Unit
) {
    composable(navDestination = HomeNavDestination) {
        HomeScreen(
            navigateToAddTaskList = navigateToAddTaskList,
            navigateToEditTaskList = navigateToEditTaskList,
            navigateToAddTask = navigateToAddTask,
            navigateToTaskDetails = navigateToTaskDetails,
            navigateToSettings = navigateToSettings,
            navigateToAbout = navigateToAbout,
            viewModel = koinInject()
        )
    }
}

private fun NavGraphBuilder.taskDetailsNode(
    navigateBack: () -> Unit,
    navigateToEditTask: (String) -> Unit
) {
    composable(navDestination = TaskDetailsNavDestination) { navBackStackEntry ->
        val taskId = TaskDetailsSafeNavArgs(navBackStackEntry).taskId.orEmpty()
        TaskDetailsScreen(
            navigateToEditTask = { navigateToEditTask(taskId) },
            navigateBack = navigateBack,
            viewModel = koinInject { parametersOf(taskId) }
        )
    }
}

private fun NavGraphBuilder.addTaskListRoute(
    navigateBack: () -> Unit
) {
    composable(navDestination = AddTaskListNavDestination) {
        AddTaskListScreen(
            navigateBack = navigateBack,
            viewModel = koinInject()
        )
    }
}

private fun NavGraphBuilder.editTaskListNode(
    navigateBack: () -> Unit
) {
    composable(navDestination = EditTaskListNavDestination) {
        EditTaskListScreen(
            navigateBack = navigateBack,
            viewModel = koinInject()
        )
    }
}

private fun NavGraphBuilder.addTaskNode(
    navigateBack: () -> Unit
) {
    composable(navDestination = AddTaskNavDestination) {
        AddTaskScreen(
            navigateBack = navigateBack,
            viewModel = koinInject()
        )
    }
}

private fun NavGraphBuilder.editTaskNode(
    navigateBack: () -> Unit
) {
    composable(navDestination = EditTaskNavDestination) { navBackStackEntry ->
        val taskId = EditTaskSafeNavArgs(navBackStackEntry).taskId.orEmpty()
        EditTaskScreen(
            navigateBack = navigateBack,
            viewModel = koinInject { parametersOf(taskId) }
        )
    }
}

private fun NavGraphBuilder.settingsNode(
    navigateBack: () -> Unit
) {
    composable(navDestination = SettingsNavDestination) {
        SettingsScreen(
            navigateBack = navigateBack,
            viewModel = koinInject()
        )
    }
}

internal expect fun NavGraphBuilder.aboutNode(
    navigateBack: () -> Unit
)
