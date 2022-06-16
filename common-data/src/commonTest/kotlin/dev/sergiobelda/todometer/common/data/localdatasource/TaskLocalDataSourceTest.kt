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

package dev.sergiobelda.todometer.common.data.localdatasource

import dev.sergiobelda.todometer.common.data.database.dao.ITaskDao
import dev.sergiobelda.todometer.common.data.database.mapper.toTask
import dev.sergiobelda.todometer.common.data.database.mapper.toTaskEntity
import dev.sergiobelda.todometer.common.data.database.mapper.toTaskItem
import dev.sergiobelda.todometer.common.domain.Result
import dev.sergiobelda.todometer.common.domain.model.TaskState
import dev.sergiobelda.todometer.common.testutils.task1
import dev.sergiobelda.todometer.common.testutils.taskEntity1
import dev.sergiobelda.todometer.common.testutils.taskItemsEntities
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TaskLocalDataSourceTest {

    @MockK
    private val taskDao: ITaskDao = mockk(relaxed = true)

    private val taskLocalDataSource = TaskLocalDataSource(taskDao)

    @Test
    fun testGetTask() = runTest {
        coEvery { taskDao.getTask("1") } returns flow {
            emit(taskEntity1)
        }

        val result = taskLocalDataSource.getTask("1").first()
        assertTrue { result is Result.Success }
        assertEquals(taskEntity1.toTask(), (result as? Result.Success)?.value)
    }

    @Test
    fun testGetTaskNotExist() = runTest {
        coEvery { taskDao.getTask("1") } returns flow {
            emit(null)
        }

        val result = taskLocalDataSource.getTask("1").first()
        assertTrue { result is Result.Error }
    }

    @Test
    fun testGetTasks() = runTest {
        coEvery { taskDao.getTasks("1") } returns flow {
            emit(taskItemsEntities)
        }

        val result = taskLocalDataSource.getTasks("1").first()
        assertTrue { result is Result.Success }
        assertEquals(
            taskItemsEntities.map { it.toTaskItem() },
            (result as? Result.Success)?.value
        )
    }

    @Test
    fun testInsertTask() = runTest {
        coEvery { taskDao.insertTask(taskEntity1) } returns taskEntity1.id

        val result = taskLocalDataSource.insertTask(taskEntity1.toTask())
        assertTrue { result is Result.Success }
    }

    @Test
    fun testUpdateTask() = runTest {
        taskLocalDataSource.updateTask(task1)

        coVerify { taskDao.updateTask(task1.toTaskEntity()) }
    }

    @Test
    fun testUpdateTaskSync() = runTest {
        taskLocalDataSource.updateTaskSync("1", sync = true)

        coVerify { taskDao.updateTaskSync("1", sync = true) }
    }

    @Test
    fun testUpdateTaskState() = runTest {
        taskLocalDataSource.updateTaskState("1", state = TaskState.DOING)

        coVerify { taskDao.updateTaskState("1", state = TaskState.DOING) }
    }

    @Test
    fun taskDeleteTask() = runTest {
        taskLocalDataSource.deleteTask("1")

        coVerify { taskDao.deleteTask("1") }
    }
}
