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

package dev.sergiobelda.todometer.common.di

import dev.sergiobelda.todometer.common.data.api.TodometerApi
import dev.sergiobelda.todometer.common.data.api.client.ITaskApiClient
import dev.sergiobelda.todometer.common.data.api.client.ITaskListApiClient
import dev.sergiobelda.todometer.common.data.api.client.TaskApiClient
import dev.sergiobelda.todometer.common.data.api.client.TaskListApiClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val networkModule = module {
    singleOf(::TodometerApi)
    single<ITaskListApiClient> {
        TaskListApiClient(get())
    }
    single<ITaskApiClient> {
        TaskApiClient(get())
    }
}
