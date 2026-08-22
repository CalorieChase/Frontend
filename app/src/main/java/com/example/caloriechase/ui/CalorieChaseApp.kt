package com.example.caloriechase.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.WorkspacePremium
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.caloriechase.ui.components.AppGradientSurface
import com.example.caloriechase.ui.screens.ActiveRunScreen
import com.example.caloriechase.ui.screens.BiometricsScreen
import com.example.caloriechase.ui.screens.CoachScreen
import com.example.caloriechase.ui.screens.DashboardPlaceholderScreen
import com.example.caloriechase.ui.screens.HomePlaceholderScreen
import com.example.caloriechase.ui.screens.LocationPickerScreen
import com.example.caloriechase.ui.screens.LoginScreen
import com.example.caloriechase.ui.screens.OnboardingScreen
import com.example.caloriechase.ui.screens.ProgressScreen
import com.example.caloriechase.ui.screens.RegisterScreen
import com.example.caloriechase.ui.screens.RoutePlannerScreen
import com.example.caloriechase.ui.screens.RoutePreviewScreen
import com.example.caloriechase.ui.screens.RunSummaryScreen
import com.example.caloriechase.ui.screens.SplashScreen
import com.example.caloriechase.ui.screens.TreasureScreen

private data class BottomDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun CalorieChaseApp(
    appViewModel: CalorieChaseViewModel = viewModel()
) {
    val navController = rememberNavController()
    val state = appViewModel.uiState
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val bottomDestinations = listOf(
        BottomDestination(AppDestination.Home.route, "Home", Icons.Rounded.Home),
        BottomDestination(AppDestination.Treasure.route, "Treasure", Icons.Rounded.WorkspacePremium),
        BottomDestination(AppDestination.Progress.route, "Progress", Icons.Rounded.Insights),
        BottomDestination(AppDestination.Coach.route, "Coach", Icons.Rounded.AutoAwesome),
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
                startDestination = AppDestination.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
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
                    OnboardingScreen(
                        modifier = Modifier.fillMaxSize(),
                        onGetStarted = { navController.navigate(AppDestination.Login.route) }
                    )
                }
                composable(AppDestination.Login.route) {
                    LoginScreen(
                        modifier = Modifier.fillMaxSize(),
                        onLogin = {
                            navController.navigate(AppDestination.Home.route) {
                                popUpTo(AppDestination.Login.route) { inclusive = true }
                            }
                        },
                        onGoToRegister = { navController.navigate(AppDestination.Register.route) }
                    )
                }
                composable(AppDestination.Register.route) {
                    RegisterScreen(
                        modifier = Modifier.fillMaxSize(),
                        onContinue = { navController.navigate(AppDestination.Biometrics.route) },
                        onGoToLogin = { navController.popBackStack() }
                    )
                }
                composable(AppDestination.Biometrics.route) {
                    BiometricsScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBack = { navController.popBackStack() },
                        onComplete = { heightCm, weightKg ->
                            appViewModel.updateBiometrics(heightCm, weightKg)
                            navController.navigate(AppDestination.Home.route) {
                                popUpTo(AppDestination.Login.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(AppDestination.Home.route) {
                    HomePlaceholderScreen(
                        profile = state.profile,
                        focusCards = state.homeFocus,
                        modifier = Modifier.fillMaxSize(),
                        onPlanRoute = { navController.navigate(AppDestination.RoutePlanner.route) }
                    )
                }
                composable(AppDestination.Treasure.route) {
                    TreasureScreen(
                        treasures = state.treasures,
                        modifier = Modifier.fillMaxSize(),
                        onPlanRoute = { navController.navigate(AppDestination.RoutePlanner.route) }
                    )
                }
                composable(AppDestination.Progress.route) {
                    ProgressScreen(
                        highlights = state.progressHighlights,
                        achievements = state.achievements,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(AppDestination.Coach.route) {
                    CoachScreen(
                        tips = state.coachTips,
                        modifier = Modifier.fillMaxSize(),
                        onPlanRoute = { navController.navigate(AppDestination.RoutePlanner.route) }
                    )
                }
                composable(AppDestination.Dashboard.route) {
                    DashboardPlaceholderScreen(
                        metrics = state.dashboardMetrics,
                        recentRuns = state.recentRuns,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(AppDestination.RoutePlanner.route) {
                    RoutePlannerScreen(
                        selectedGoalType = state.selectedGoalType,
                        selectedGoalValue = state.selectedGoalValue,
                        selectedRouteType = state.selectedRouteType,
                        selectedLocation = state.selectedLocation,
                        weightKg = state.profile.weightKg,
                        isGeneratingRoute = state.isGeneratingRoute,
                        routeErrorMessage = state.routeErrorMessage,
                        modifier = Modifier.fillMaxSize(),
                        onBack = { navController.popBackStack() },
                        onOpenLocationPicker = { navController.navigate(AppDestination.LocationPicker.route) },
                        onSelectGoalType = appViewModel::selectGoalType,
                        onGoalValueChange = appViewModel::selectGoalValue,
                        onSelectRouteType = appViewModel::selectRouteType,
                        onDismissError = appViewModel::clearRouteError,
                        onGenerateRoute = {
                            appViewModel.generateRoute {
                                navController.navigate(AppDestination.RoutePreview.route) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
                composable(AppDestination.LocationPicker.route) {
                    LocationPickerScreen(
                        selectedLocation = state.selectedLocation,
                        modifier = Modifier.fillMaxSize(),
                        onBack = { navController.popBackStack() },
                        onSelectLocation = appViewModel::selectLocation,
                        onConfirm = { navController.popBackStack() }
                    )
                }
                composable(AppDestination.RoutePreview.route) {
                    RoutePreviewScreen(
                        routePreview = state.activeRoute,
                        modifier = Modifier.fillMaxSize(),
                        onBack = { navController.popBackStack() },
                        onRemixRoute = { navController.popBackStack() },
                        onStartRun = {
                            appViewModel.startRunSession()
                            navController.navigate(AppDestination.ActiveRun.route)
                        }
                    )
                }
                composable(AppDestination.ActiveRun.route) {
                    ActiveRunScreen(
                        routePreview = state.activeRoute,
                        runSession = state.runSession,
                        weightKg = state.profile.weightKg,
                        modifier = Modifier.fillMaxSize(),
                        onBack = {
                            appViewModel.cancelRunSession()
                            navController.popBackStack()
                        },
                        onLocationUpdate = appViewModel::updateRunLocation,
                        onCoinCelebrationShown = appViewModel::clearLastCollectedCoinValue,
                        onFinishRun = {
                            appViewModel.finishRunSession()
                            navController.navigate(AppDestination.RunSummary.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(AppDestination.RunSummary.route) {
                    RunSummaryScreen(
                        routePreview = state.activeRoute,
                        runSession = state.runSession,
                        weightKg = state.profile.weightKg,
                        modifier = Modifier.fillMaxSize(),
                        onBackHome = {
                            appViewModel.cancelRunSession()
                            navController.navigate(AppDestination.Home.route) {
                                popUpTo(AppDestination.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onTryAnother = {
                            appViewModel.cancelRunSession()
                            navController.navigate(AppDestination.RoutePlanner.route) {
                                popUpTo(AppDestination.Home.route)
                            }
                        }
                    )
                }
            }
        }
    }
}
