// File: com/shivangi.mealrecipe/views/InstructionList.kt
// One line space between instructions

package com.shivangi.mealrecipe.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun InstructionList(instructions: String) {
    val lines = instructions.split("\n").filter { it.isNotBlank() }
    Column(Modifier.padding(start = 8.dp)) {
        lines.forEachIndexed { i, line ->
            Text(
                text = "${i + 1}. $line",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
