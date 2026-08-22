package com.example.caloriechase.ui

sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Onboarding : AppDestination("onboarding")
    data object Login : AppDestination("login")
    data object Register : AppDestination("register")
    data object Biometrics : AppDestination("biometrics")
    data object Home : AppDestination("home")
    data object Treasure : AppDestination("treasure")
    data object Progress : AppDestination("progress")
    data object Coach : AppDestination("coach")
    data object Dashboard : AppDestination("dashboard")
    data object RoutePlanner : AppDestination("route_planner")
    data object LocationPicker : AppDestination("location_picker")
    data object RoutePreview : AppDestination("route_preview")
    data object ActiveRun : AppDestination("active_run")
    data object RunSummary : AppDestination("run_summary")
}
