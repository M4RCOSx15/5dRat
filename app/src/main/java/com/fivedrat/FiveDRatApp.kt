package com.fivedrat

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fivedrat.ui.navigation.AppDestination
import com.fivedrat.ui.screen.PlaceholderScreen
import com.fivedrat.ui.screen.home.HomeScreen
import com.fivedrat.ui.screen.lab.LabScreen
import com.fivedrat.ui.screen.missions.MissionsScreen
import com.fivedrat.ui.screen.profile.ProfileScreen
import com.fivedrat.ui.theme.FiveDRatTheme

@Composable
fun FiveDRatApp() {
    FiveDRatTheme {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        Scaffold(
            bottomBar = {
                NavigationBar {
                    AppDestination.entries.forEach { destination ->
                        NavigationBarItem(
                            selected = currentRoute == destination.route,
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
                                    contentDescription = stringResource(destination.labelRes),
                                )
                            },
                            label = { Text(stringResource(destination.labelRes)) },
                        )
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppDestination.Home.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                AppDestination.entries.forEach { destination ->
                    composable(destination.route) {
                        when (destination) {
                            AppDestination.Home ->
                                HomeScreen(onNavigate = { route -> navController.navigate(route) })
                            AppDestination.Missions ->
                                MissionsScreen()
                            AppDestination.Lab ->
                                LabScreen()
                            AppDestination.Profile ->
                                ProfileScreen()
                            else ->
                                PlaceholderScreen(title = stringResource(destination.labelRes))
                        }
                    }
                }
            }
        }
    }
}
