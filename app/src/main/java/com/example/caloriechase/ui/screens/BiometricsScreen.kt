package com.example.caloriechase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.CalorieTextField
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.ScreenHeader
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonGreen
import com.example.caloriechase.ui.theme.NeonOrange

@Composable
fun BiometricsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onComplete: (heightCm: Int, weightKg: Int) -> Unit
) {
    var height by remember { mutableStateOf("178") }
    var weight by remember { mutableStateOf("75") }

    ScreenColumn(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Complete your profile",
            subtitle = "Use placeholder biometrics for now so calorie and route estimates still feel personalized.",
            onBack = onBack
        )

        SurfacePanel(emphasized = true) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Body metrics",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                BodyText("These values stay local in the placeholder flow until backend wiring is ready.")
                CalorieTextField(
                    value = height,
                    onValueChange = { height = it.filter(Char::isDigit) },
                    label = "Height (cm)",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                CalorieTextField(
                    value = weight,
                    onValueChange = { weight = it.filter(Char::isDigit) },
                    label = "Weight (kg)",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
        }

        SurfacePanel {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricNote("Suggested", "178 cm", NeonOrange, Modifier.weight(1f))
                MetricNote("Recommended", "75 kg", NeonGreen, Modifier.weight(1f))
            }
        }

        SurfacePanel {
            Text(
                text = "These placeholder values can be changed later once real account persistence is wired in.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        PrimaryButton(
            text = "Finish setup",
            onClick = {
                onComplete(height.toIntOrNull() ?: 178, weight.toIntOrNull() ?: 75)
            }
        )

        BodyText(
            text = "You can treat this as a frontend-only profile step until data persistence is added.",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun MetricNote(
    label: String,
    value: String,
    accent: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = accent)
        Text(text = value, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
    }
}
