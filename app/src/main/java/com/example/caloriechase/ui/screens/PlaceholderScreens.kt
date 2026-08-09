package com.example.caloriechase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingPlaceholderScreen(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit
) {
    PlaceholderScreen(
        modifier = modifier,
        eyebrow = "Onboarding",
        title = "Five-screen onboarding flow will land here next.",
        description = "I’ve wired the navigation first so we can migrate each screen with clean commits and keep the app runnable the whole way."
    ) {
        Button(onClick = onGetStarted) {
            Text("Continue to login")
        }
    }
}

@Composable
fun AuthPlaceholderScreen(
    modifier: Modifier = Modifier,
    isRegister: Boolean,
    onPrimaryAction: () -> Unit,
    onSwitchMode: () -> Unit
) {
    PlaceholderScreen(
        modifier = modifier,
        eyebrow = if (isRegister) "Register" else "Login",
        title = if (isRegister) "Registration screen coming next." else "Login screen coming next.",
        description = "The old XML auth flow is now connected in Compose. This placeholder will be replaced with the polished form layout in the next commit."
    ) {
        Button(onClick = onPrimaryAction) {
            Text(if (isRegister) "Mock continue" else "Mock sign in")
        }
        OutlinedButton(onClick = onSwitchMode) {
            Text(if (isRegister) "Back to login" else "Go to register")
        }
    }
}

@Composable
fun HomePlaceholderScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        modifier = modifier,
        eyebrow = "Home",
        title = "Home dashboard migration in progress.",
        description = "This route is already wired into the new Compose bottom navigation and will be replaced with the route preview, weekly rhythm, and stat panels next."
    )
}

@Composable
fun DashboardPlaceholderScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        modifier = modifier,
        eyebrow = "Dashboard",
        title = "Performance dashboard migration in progress.",
        description = "The summary cards and recent runs list are the next pieces coming over from the old Java/XML frontend."
    )
}

@Composable
private fun PlaceholderScreen(
    modifier: Modifier = Modifier,
    eyebrow: String,
    title: String,
    description: String,
    actions: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = eyebrow,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp)
        )
        if (actions != null) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 24.dp)
            ) {
                actions()
            }
        }
    }
}
