package com.example.caloriechase.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.caloriechase.ui.components.BodyText
import com.example.caloriechase.ui.components.CalorieTextField
import com.example.caloriechase.ui.components.EyebrowText
import com.example.caloriechase.ui.components.PrimaryButton
import com.example.caloriechase.ui.components.ScreenColumn
import com.example.caloriechase.ui.components.SurfacePanel
import com.example.caloriechase.ui.theme.NeonBlue
import com.example.caloriechase.ui.theme.NeonOrange

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onGoToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    ScreenColumn(modifier = modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EyebrowText("CalorieChase", color = NeonBlue)
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            BodyText("Pick up where you left off, review your progress, and start your next run fast.")
        }

        SurfacePanel(emphasized = true) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Sign in",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                BodyText("Use your account details to continue.")
                CalorieTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email address",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                CalorieTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Rounded.VisibilityOff
                                } else {
                                    Icons.Rounded.Visibility
                                },
                                contentDescription = if (passwordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                            )
                        }
                    }
                )
            }
        }

        PrimaryButton(text = "Log in", onClick = onLogin)

        BodyText(
            text = "Secure sign-in with your running profile",
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        AuthSwitchPanel(
            message = "New here? Create your account and set up your profile in under a minute.",
            actionLabel = "Sign up",
            actionColor = NeonOrange,
            onAction = onGoToRegister
        )
    }
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit,
    onGoToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    ScreenColumn(modifier = modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Create your account",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            BodyText("Set up your profile once so runs, calories, and progress feel personalized from day one.")
        }

        SurfacePanel(emphasized = true) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "About you",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                BodyText("These details help create a better route and coaching experience.")
                CalorieTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full name",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                CalorieTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email address",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                CalorieTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Rounded.VisibilityOff
                                } else {
                                    Icons.Rounded.Visibility
                                },
                                contentDescription = if (passwordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                            )
                        }
                    }
                )
            }
        }

        PrimaryButton(text = "Continue setup", onClick = onContinue)

        BodyText("Next, we'll capture height and weight for more accurate calorie estimates.")

        AuthSwitchPanel(
            message = "Already have an account? Jump back into your routine.",
            actionLabel = "Log in",
            actionColor = NeonBlue,
            onAction = onGoToLogin
        )
    }
}

@Composable
private fun AuthSwitchPanel(
    message: String,
    actionLabel: String,
    actionColor: androidx.compose.ui.graphics.Color,
    onAction: () -> Unit
) {
    SurfacePanel {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            BodyText(message)
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.labelLarge,
                color = actionColor,
                modifier = Modifier.clickable(onClick = onAction)
            )
        }
    }
}
