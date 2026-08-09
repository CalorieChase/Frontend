package com.example.caloriechase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.LocationSuggestion
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.ScreenHeader
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.SurfaceOutline

@Composable
fun LocationPickerScreen(
    suggestions: List<LocationSuggestion>,
    selectedLocation: LocationSuggestion,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSelectLocation: (LocationSuggestion) -> Unit,
    onConfirm: () -> Unit
) {
    ScreenColumn(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Choose a start point",
            subtitle = "Backend map search is deferred, so this screen uses curated placeholders that still match the legacy flow.",
            onBack = onBack
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                .border(1.dp, SurfaceOutline, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Map Preview Placeholder",
                style = MaterialTheme.typography.titleMedium,
                color = NeonBlue
            )
        }

        suggestions.forEach { suggestion ->
            val selected = suggestion == selectedLocation
            SurfacePanel(
                modifier = Modifier.clickable { onSelectLocation(suggestion) },
                emphasized = selected
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = suggestion.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    BodyText(suggestion.address)
                    Text(
                        text = suggestion.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selected) NeonBlue else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (selected) {
                        Text(
                            text = "Selected starting point",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonBlue
                        )
                    }
                }
            }
        }

        PrimaryButton(text = "Use this starting point", onClick = onConfirm)
    }
}
