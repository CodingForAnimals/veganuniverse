package org.codingforanimals.map.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun MapFloatingActionButton() {
    FloatingActionButton(onClick = { }) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Agregar")
    }
}