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

package dev.sergiobelda.todometer.wear

import androidx.navigation.NavHostController
import dev.sergiobelda.todometer.wear.Destinations.AddTask
import dev.sergiobelda.todometer.wear.Destinations.AddTaskList
import dev.sergiobelda.todometer.wear.Destinations.TaskListTasks

object Destinations {
    const val Home = "home"
    const val AddTaskList = "addTaskList"
    const val TaskListTasks = "taskListTasks"
    const val AddTask = "addTask"

    object TaskListTasksArgs {
        const val TaskListId = "taskListId"
    }
}

class Actions(navController: NavHostController) {
    val openTaskList: (String) -> Unit = { taskListId ->
        navController.navigate("$TaskListTasks/$taskListId")
    }
    val addTaskList: () -> Unit = { navController.navigate(AddTaskList) }
    val addTask: (String) -> Unit = { taskListId ->
        navController.navigate("$AddTask/$taskListId")
    }
    val navigateUp: () -> Unit = { navController.popBackStack() }
}
