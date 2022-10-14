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

package dev.sergiobelda.todometer.wear.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import dev.sergiobelda.todometer.common.android.navigation.Action
import dev.sergiobelda.todometer.wear.ui.deletetask.DeleteTaskDestination
import dev.sergiobelda.todometer.wear.ui.deletetask.DeleteTaskScreen
import dev.sergiobelda.todometer.wear.ui.deletetask.navigateToDeleteTask
import dev.sergiobelda.todometer.wear.ui.deletetasklist.DeleteTaskListDestination
import dev.sergiobelda.todometer.wear.ui.deletetasklist.DeleteTaskListScreen
import dev.sergiobelda.todometer.wear.ui.deletetasklist.navigateToDeleteTaskList
import dev.sergiobelda.todometer.wear.ui.home.HomeDestination
import dev.sergiobelda.todometer.wear.ui.home.HomeScreen
import dev.sergiobelda.todometer.wear.ui.home.popBackStackToHome
import dev.sergiobelda.todometer.wear.ui.taskdetail.TaskDetailDestination
import dev.sergiobelda.todometer.wear.ui.taskdetail.TaskDetailScreen
import dev.sergiobelda.todometer.wear.ui.taskdetail.navigateToTaskDetail
import dev.sergiobelda.todometer.wear.ui.tasklisttasks.TaskListTasksDestination
import dev.sergiobelda.todometer.wear.ui.tasklisttasks.TaskListTasksScreen
import dev.sergiobelda.todometer.wear.ui.tasklisttasks.navigateToTaskListTasks

@Composable
fun ToDometerNavHost(
    navController: NavHostController,
    action: Action,
    modifier: Modifier = Modifier
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(HomeDestination.route) {
            HomeScreen(openTaskList = action.navigateToTaskListTasks)
        }
        composable(
            TaskListTasksDestination.route,
            arguments = listOf(TaskListTasksDestination.taskListIdNavArgument)
        ) { navBackStackEntry ->
            val taskListId = TaskListTasksDestination.navArgsTaskListId(navBackStackEntry)
            TaskListTasksScreen(
                taskListId = taskListId,
                openTask = action.navigateToTaskDetail,
                deleteTaskList = { action.navigateToDeleteTaskList(taskListId) }
            )
        }
        composable(DeleteTaskListDestination.route) { navBackStackEntry ->
            val taskListId = DeleteTaskListDestination.navArgsTaskListId(navBackStackEntry)
            DeleteTaskListScreen(
                taskListId = taskListId,
                onDeleteTaskList = action.popBackStackToHome,
                navigateBack = { action.popBackStack() }
            )
        }
        composable(TaskDetailDestination.route) { navBackStackEntry ->
            val taskId = TaskDetailDestination.navArgsTaskId(navBackStackEntry)
            TaskDetailScreen(
                taskId = taskId,
                deleteTask = { action.navigateToDeleteTask(taskId) }
            )
        }
        composable(DeleteTaskDestination.route) { navBackStackEntry ->
            val taskId = DeleteTaskDestination.navArgsTaskId(navBackStackEntry)
            DeleteTaskScreen(
                taskId = taskId,
                onDeleteTask = {
                    action.popBackStack()
                    action.navigateUp()
                },
                navigateBack = { action.popBackStack() }
            )
        }
    }
}
