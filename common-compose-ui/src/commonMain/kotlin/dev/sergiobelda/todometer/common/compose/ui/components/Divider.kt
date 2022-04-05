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

package dev.sergiobelda.todometer.common.compose.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sergiobelda.todometer.common.compose.ui.theme.TodometerColors
import dev.sergiobelda.todometer.common.compose.ui.theme.outline

@Composable
fun HorizontalDivider(modifier: Modifier = Modifier) {
    Divider(thickness = 1.dp, color = TodometerColors.outline, modifier = modifier)
}

@Composable
fun VerticalDivider(modifier: Modifier = Modifier) {
    Divider(
        thickness = 1.dp,
        color = TodometerColors.outline,
        modifier = modifier.fillMaxHeight().width(1.dp)
    )
}
