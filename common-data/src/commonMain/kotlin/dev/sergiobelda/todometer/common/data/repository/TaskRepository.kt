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

package dev.sergiobelda.todometer.common.data.repository

import dev.sergiobelda.todometer.common.data.localdatasource.ITaskLocalDataSource
import dev.sergiobelda.todometer.common.data.remotedatasource.ITaskRemoteDataSource
import dev.sergiobelda.todometer.common.data.util.randomUUIDString
import dev.sergiobelda.todometer.common.domain.Result
import dev.sergiobelda.todometer.common.domain.doIfSuccess
import dev.sergiobelda.todometer.common.domain.model.Tag
import dev.sergiobelda.todometer.common.domain.model.Task
import dev.sergiobelda.todometer.common.domain.model.TaskState
import dev.sergiobelda.todometer.common.domain.repository.ITaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * Repository for performing [Task] data operations.
 */
class TaskRepository(
    private val taskLocalDataSource: ITaskLocalDataSource,
    private val taskRemoteDataSource: ITaskRemoteDataSource
) : ITaskRepository {

    override fun getTask(id: String): Flow<Result<Task>> =
        taskLocalDataSource.getTask(id)

    override fun getTasks(taskListId: String): Flow<Result<List<Task>>> =
        taskLocalDataSource.getTasks(taskListId)
    /*
    taskLocalDataSource.getTasks(taskListId).map { result ->
        result.doIfSuccess { tasks ->
            synchronizeTasksRemotely(tasks.filter { !it.sync })
            // TODO Remove ?: ""
            refreshTasks(taskListId ?: "")
        }
    }
    */

    /**
     * Synchronize a list of [Task] remotely.
     * For each task, calls insert to remote service and if it goes successful
     * sets sync flag to true.
     */
    private suspend fun synchronizeTasksRemotely(tasks: List<Task>) {
        tasks.forEach { task ->
            // TODO Maybe use Update
            val result = taskRemoteDataSource.insertTask(
                id = task.id,
                title = task.title,
                description = task.description,
                taskListId = task.taskListId,
                state = task.state,
                tag = task.tag
            )
            result.doIfSuccess {
                taskLocalDataSource.updateTaskSync(task.id, true)
            }
        }
    }

    override suspend fun refreshTasks(taskListId: String) {
        val result = taskRemoteDataSource.getTasks(taskListId)
        result.doIfSuccess { list ->
            taskLocalDataSource.insertTasks(list)
        }
    }

    /**
     * Depending on whether the remote call is successful or not,
     * inserts this task into the local database with true or false sync flag.
     */
    override suspend fun insertTask(
        title: String,
        description: String,
        taskListId: String,
        tag: Tag
    ): Result<String> {
        val taskId = randomUUIDString()
        val sync = false
        /*
        taskRemoteDataSource.insertTask(
            title = title, description = description, taskListId = taskListId, tag = tag
        ).doIfSuccess {
            taskId = it
            sync = true
        }.doIfError {
            taskId = randomUUIDString()
        }
        */
        return taskLocalDataSource.insertTask(
            Task(
                id = taskId,
                title = title,
                description = description,
                state = TaskState.DOING,
                taskListId = taskListId,
                tag = tag,
                sync = sync
            )
        )
    }

    override suspend fun updateTask(task: Task) = taskLocalDataSource.updateTask(task)

    /**
     * Update Task state locally and remotely. If remote call returns an error,
     * sync flag for this task is set to false in local database.
     */
    override suspend fun updateTaskState(id: String, state: TaskState) {
        taskLocalDataSource.updateTaskState(id, state)
        /*
        taskRemoteDataSource.updateTaskState(id, state).doIfError {
            taskLocalDataSource.updateTaskSync(id, false)
        }
        */
    }

    /**
     * It only removes task from local database if remote call is successful.
     */
    override suspend fun deleteTask(id: String) = taskLocalDataSource.deleteTask(id)
    /*
    taskRemoteDataSource.deleteTask(id).doIfSuccess {
        taskLocalDataSource.deleteTask(id)
    }
    */
}
