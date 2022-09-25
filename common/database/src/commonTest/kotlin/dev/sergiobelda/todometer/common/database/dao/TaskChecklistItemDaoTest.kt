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

package dev.sergiobelda.todometer.common.database.dao

import dev.sergiobelda.todometer.common.database.DatabaseTest
import dev.sergiobelda.todometer.common.database.testutils.taskChecklistItemEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskChecklistItemDaoTest : DatabaseTest() {

    private lateinit var taskChecklistItemDao: ITaskChecklistItemDao

    @BeforeTest
    fun init() {
        taskChecklistItemDao = TaskChecklistItemDao(database)
    }

    @Test
    fun testInsertTaskChecklistItems() = runTest {
        taskChecklistItemDao.insertTaskChecklistItems(taskChecklistItemEntity)
        val taskChecklistItems =
            taskChecklistItemDao.getTaskChecklistItems(taskChecklistItemEntity.task_id).first()
        taskChecklistItems.contains(taskChecklistItemEntity)
    }
}
