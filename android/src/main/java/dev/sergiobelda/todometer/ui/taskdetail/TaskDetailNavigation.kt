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

package dev.sergiobelda.todometer.ui.taskdetail

import androidx.navigation.NavBackStackEntry
import androidx.navigation.navDeepLink
import dev.sergiobelda.todometer.common.android.navigation.Action
import dev.sergiobelda.todometer.common.android.navigation.Destination
import dev.sergiobelda.todometer.common.android.navigation.NavigationParams

object TaskDetailDestination : Destination {
    private const val TaskIdArg = "taskId"
    const val TaskDetail = "taskdetail"

    override val route: String = "$TaskDetail/{$TaskIdArg}"

    private const val TaskDetailDeepLink = "app://open.task"

    val taskDetailNavDeepLink = navDeepLink { uriPattern = "$TaskDetailDeepLink/{$TaskIdArg}" }

    fun navArgsTaskId(navBackStackEntry: NavBackStackEntry): String =
        navBackStackEntry.arguments?.getString(TaskIdArg) ?: ""
}

class TaskDetailNavigationParams(taskId: String) : NavigationParams(TaskDetailDestination) {
    override val navigationRoute: String = "${TaskDetailDestination.TaskDetail}/$taskId"
}

val Action.navigateToTaskDetail: (String) -> Unit
    get() = { taskId ->
        navigate(TaskDetailNavigationParams(taskId))
    }
