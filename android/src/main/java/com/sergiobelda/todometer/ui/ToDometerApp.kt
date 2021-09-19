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

package com.sergiobelda.todometer.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.sergiobelda.todometer.ui.Destinations.About
import com.sergiobelda.todometer.ui.Destinations.AddProject
import com.sergiobelda.todometer.ui.Destinations.AddTask
import com.sergiobelda.todometer.ui.Destinations.EditProject
import com.sergiobelda.todometer.ui.Destinations.EditTask
import com.sergiobelda.todometer.ui.Destinations.Home
import com.sergiobelda.todometer.ui.Destinations.TaskDetail
import com.sergiobelda.todometer.ui.Destinations.TaskDetailArgs.TaskId
import com.sergiobelda.todometer.ui.about.AboutScreen
import com.sergiobelda.todometer.ui.addproject.AddProjectScreen
import com.sergiobelda.todometer.ui.addtask.AddTaskScreen
import com.sergiobelda.todometer.ui.editproject.EditProjectScreen
import com.sergiobelda.todometer.ui.edittask.EditTaskScreen
import com.sergiobelda.todometer.ui.home.HomeScreen
import com.sergiobelda.todometer.ui.taskdetail.TaskDetailScreen
import com.sergiobelda.todometer.ui.theme.ToDometerTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun ToDometerApp() {
    val mainViewModel: MainViewModel = getViewModel()
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }

    val appTheme = mainViewModel.appTheme.collectAsState()

    ToDometerTheme(isSystemInDarkTheme()) {
        NavHost(navController, startDestination = Home) {
            composable(Home) {
                HomeScreen(
                    actions.addProject,
                    actions.editProject,
                    actions.addTask,
                    actions.openTask,
                    actions.about
                )
            }
            composable(
                "$TaskDetail/{$TaskId}",
                arguments = listOf(navArgument(TaskId) { type = NavType.StringType })
            ) { navBackStackEntry ->
                TaskDetailScreen(
                    taskId = navBackStackEntry.arguments?.getString(TaskId) ?: "",
                    actions.editTask,
                    actions.navigateUp
                )
            }
            composable(AddProject) {
                AddProjectScreen(actions.navigateUp)
            }
            composable(EditProject) {
                EditProjectScreen(actions.navigateUp)
            }
            composable(AddTask) {
                AddTaskScreen(actions.navigateUp)
            }
            composable(
                "$EditTask/{$TaskId}",
                arguments = listOf(navArgument(TaskId) { type = NavType.StringType })
            ) { backStackEntry ->
                EditTaskScreen(
                    taskId = backStackEntry.arguments?.getString(TaskId) ?: "",
                    actions.navigateUp
                )
            }
            composable(About) {
                AboutScreen(
                    {},
                    {},
                    actions.navigateUp
                )
            }
        }
    }
}
