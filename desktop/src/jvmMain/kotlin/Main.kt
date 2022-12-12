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

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import dev.sergiobelda.todometer.common.core.di.initKoin
import ui.home.HomeScreen
import ui.icons.iconToDometer
import ui.task.TaskDetailScreen
import ui.theme.ToDometerAppTheme

val koin = initKoin().koin


internal class Navigator(startDestination: Screen) {
    var currentPage: Screen by mutableStateOf(Screen.Home)
        private set

    fun navigateTo(screen: Screen) {
        currentPage = screen
    }
}

fun main() = application {
    Window(
        resizable = false,
        onCloseRequest = ::exitApplication,
        title = "ToDometer",
        state = WindowState(
            size = DpSize(600.dp, 800.dp),
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        icon = iconToDometer()
    ) {
        val navigator by remember { mutableStateOf(Navigator(Screen.Home)) }

        val navigateToTaskDetail: () -> Unit = {
            navigator.navigateTo(Screen.TaskDetail)
        }
        ToDometerAppTheme {
            Crossfade(navigator.currentPage) { screen ->
                when (screen) {
                    Screen.Home -> HomeScreen(navigateToTaskDetail)
                    Screen.TaskDetail -> TaskDetailScreen(navigator.navigateToHome)
                }
            }
        }
    }
}

internal val Navigator.navigateToHome: () -> Unit
    get() = {
        navigateTo(Screen.Home)
    }
