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

package dev.sergiobelda.todometer.common.testutils

import dev.sergiobelda.todometer.ProjectEntity
import dev.sergiobelda.todometer.TaskEntity
import dev.sergiobelda.todometer.common.model.Tag
import dev.sergiobelda.todometer.common.model.TaskState

val projectEntity1 = ProjectEntity(
    id = "1",
    name = "Project 1",
    description = "Description",
    sync = false
)

val projectEntity1Updated = ProjectEntity(
    id = "1",
    name = "Project 1 Updated",
    description = "Description",
    sync = false
)

val projectEntity2 = ProjectEntity(
    id = "2",
    name = "Project 2",
    description = "Description",
    sync = false
)

val entityProjects = listOf(projectEntity1, projectEntity2)

val taskEntity1 = TaskEntity(
    id = "1",
    title = "Task 1",
    description = "Description 1",
    state = TaskState.DOING.name,
    project_id = "1",
    tag = Tag.GRAY.name,
    sync = false
)

val taskEntity1Updated = TaskEntity(
    id = "1",
    title = "Task 1 Updated",
    description = "Description 1 Updated",
    state = TaskState.DOING.name,
    project_id = "1",
    tag = Tag.RED.name,
    sync = false
)

val taskEntity2 = TaskEntity(
    id = "2",
    title = "Task 2",
    description = "Description 2",
    state = TaskState.DOING.name,
    project_id = "1",
    tag = Tag.RED.name,
    sync = false
)

val entityTasks = listOf(taskEntity1, taskEntity2)
