// File: com/shivangi.mealrecipe/views/InstructionList.kt

package com.shivangi.mealrecipe.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InstructionList(instructions: String) {
    val instructionsLines = instructions.split("\n").filter { it.isNotBlank() }
    Column(modifier = Modifier.padding(start = 8.dp)) {
        instructionsLines.forEachIndexed { index, line ->
            Text(
                text = "${index + 1}. $line",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                // 1 line space between each step
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
