package com.example.caloriechase.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.caloriechase.ui.components.AppGradientSurface
import com.example.caloriechase.ui.screens.AuthPlaceholderScreen
import com.example.caloriechase.ui.screens.DashboardPlaceholderScreen
import com.example.caloriechase.ui.screens.HomePlaceholderScreen
import com.example.caloriechase.ui.screens.OnboardingPlaceholderScreen
import com.example.caloriechase.ui.screens.SplashScreen

private data class BottomDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun CalorieChaseApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val bottomDestinations = listOf(
        BottomDestination(AppDestination.Home.route, "Home", Icons.Rounded.Home),
        BottomDestination(AppDestination.Dashboard.route, "Dashboard", Icons.Rounded.Dashboard)
    )
    val showBottomBar = currentRoute in bottomDestinations.map { it.route }

    AppGradientSurface {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        bottomDestinations.forEach { destination ->
                            val selected = currentRoute == destination.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(destination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = destination.label
                                    )
                                },
                                label = { Text(destination.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppDestination.Splash.route,
                modifier = Modifier.fillMaxSize(),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(durationMillis = 350)
                    ) + fadeIn()
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(durationMillis = 350)
                    ) + fadeOut()
                },
                popEnterTransition = {
                    slideInHorizontally(
                        animationSpec = tween(300),
                        initialOffsetX = { -it / 3 }
                    ) + fadeIn()
                },
                popExitTransition = {
                    slideOutHorizontally(
                        animationSpec = tween(300),
                        targetOffsetX = { it / 3 }
                    ) + fadeOut()
                }
            ) {
                composable(AppDestination.Splash.route) {
                    SplashScreen(
                        onFinished = {
                            navController.navigate(AppDestination.Onboarding.route) {
                                popUpTo(AppDestination.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(AppDestination.Onboarding.route) {
                    OnboardingPlaceholderScreen(
                        modifier = Modifier.fillMaxSize(),
                        onGetStarted = { navController.navigate(AppDestination.Login.route) }
                    )
                }
                composable(AppDestination.Login.route) {
                    AuthPlaceholderScreen(
                        modifier = Modifier.fillMaxSize(),
                        isRegister = false,
                        onPrimaryAction = {
                            navController.navigate(AppDestination.Home.route) {
                                popUpTo(AppDestination.Login.route) { inclusive = true }
                            }
                        },
                        onSwitchMode = { navController.navigate(AppDestination.Register.route) }
                    )
                }
                composable(AppDestination.Register.route) {
                    AuthPlaceholderScreen(
                        modifier = Modifier.fillMaxSize(),
                        isRegister = true,
                        onPrimaryAction = {
                            navController.navigate(AppDestination.Home.route) {
                                popUpTo(AppDestination.Register.route) { inclusive = true }
                            }
                        },
                        onSwitchMode = { navController.popBackStack() }
                    )
                }
                composable(AppDestination.Home.route) {
                    HomePlaceholderScreen(modifier = Modifier.fillMaxSize())
                }
                composable(AppDestination.Dashboard.route) {
                    DashboardPlaceholderScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
