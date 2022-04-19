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

package dev.sergiobelda.todometer.common.compose.ui.task

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import dev.sergiobelda.todometer.common.compose.ui.components.HorizontalDivider
import dev.sergiobelda.todometer.common.compose.ui.mapper.composeColorOf
import dev.sergiobelda.todometer.common.compose.ui.theme.TodometerColors
import dev.sergiobelda.todometer.common.compose.ui.theme.onSurfaceMediumEmphasis
import dev.sergiobelda.todometer.common.domain.model.Task
import dev.sergiobelda.todometer.common.domain.model.TaskState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: Task,
    onDoingClick: (String) -> Unit,
    onDoneClick: (String) -> Unit,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.combinedClickable(
            onClick = {
                onClick(task.id)
            },
            onLongClick = {
                onLongClick(task.id)
            }
        ).fillMaxWidth().background(TodometerColors.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp, end = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(TodometerColors.composeColorOf(task.tag))
            )
            when (task.state) {
                TaskState.DOING -> {
                    Text(
                        task.title,
                        modifier = Modifier.padding(start = 8.dp).weight(1f),
                        maxLines = 1
                    )
                    IconButton(
                        onClick = { onDoneClick(task.id) }
                    ) {
                        Icon(
                            Icons.Rounded.Check,
                            contentDescription = "Done",
                            tint = TodometerColors.secondary
                        )
                    }
                }
                TaskState.DONE -> {
                    Text(
                        task.title,
                        textDecoration = TextDecoration.LineThrough,
                        color = TodometerColors.onSurfaceMediumEmphasis,
                        modifier = Modifier.padding(start = 8.dp).weight(1f),
                        maxLines = 1
                    )
                    IconButton(
                        onClick = { onDoingClick(task.id) }
                    ) {
                        Icon(
                            Icons.Filled.Replay,
                            contentDescription = "Doing",
                            tint = TodometerColors.secondary
                        )
                    }
                }
            }
        }
        TaskItemAdditionalInformationRow(task)
        HorizontalDivider()
    }
}

@Composable
internal fun TaskItemAdditionalInformationRow(task: Task) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 36.dp, end = 8.dp)
    ) {
        if (task.state == TaskState.DOING) {
            task.dueDate?.let { dueDate ->
                TaskDueDateChip(dueDate, modifier = Modifier.padding(bottom = 8.dp))
            }
        }
    }
}
