package com.example.caloriechase.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.SurfacePanel

@Composable
fun HomePlaceholderScreen(modifier: Modifier = Modifier) {
    ScreenColumn(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        SurfacePanel(emphasized = true) {
            Text(
                text = "Home screen migration is next.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        BodyText(
            text = "The weekly rhythm, route preview, and guided-run CTA will replace this placeholder in the next commit.",
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun DashboardPlaceholderScreen(modifier: Modifier = Modifier) {
    ScreenColumn(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        SurfacePanel(emphasized = true) {
            Text(
                text = "Performance dashboard migration is next.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        BodyText(
            text = "The stats cards and recent runs list will land here in the next commit.",
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
