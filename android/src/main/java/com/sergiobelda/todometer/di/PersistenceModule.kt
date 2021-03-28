/*
 * Copyright 2020 Sergio Belda
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

package com.sergiobelda.todometer.di

import androidx.room.Room
import com.sergiobelda.todometer.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

// TODO Remove - Moved to common module
val persistenceModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "database"
        ).build()
    }
    single { get<AppDatabase>().projectDao() }
    single { get<AppDatabase>().taskDao() }
    single { get<AppDatabase>().tagDao() }
    single { get<AppDatabase>().taskProjectDao() }
}
