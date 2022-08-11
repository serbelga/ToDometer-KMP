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

package dev.sergiobelda.todometer.common.compose.ui.tasklist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.sergiobelda.todometer.common.compose.ui.theme.ToDometerTheme

@Composable
fun TaskListItem(
    text: String,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    val background = if (isSelected) {
        Modifier.background(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            shape = MaterialTheme.shapes.extraLarge
        )
    } else {
        Modifier
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(56.dp).clip(MaterialTheme.shapes.extraLarge)
            .selectable(isSelected, onClick = onItemClick)
            .then(background)
    ) {
        val selectedColor =
            if (isSelected) MaterialTheme.colorScheme.primary else ToDometerTheme.toDometerColors.onSurfaceMediumEmphasis
        Text(
            text = text,
            color = selectedColor,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            modifier = Modifier.weight(1f).padding(start = 16.dp, end = 16.dp)
        )
    }
}
