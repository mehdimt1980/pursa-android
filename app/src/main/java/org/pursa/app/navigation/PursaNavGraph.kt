package org.pursa.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.pursa.app.feature.home.HomeScreen
import org.pursa.app.feature.home.PursaWorlds
import org.pursa.app.feature.world.InvalidWorldScreen
import org.pursa.app.feature.world.WorldDetailScreen
import org.pursa.app.navigation.PursaDestination.Home
import org.pursa.app.navigation.PursaDestination.Welcome
import org.pursa.app.navigation.PursaDestination.WorldDetail

@Composable
fun PursaNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Welcome.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Welcome.route) {
            WelcomeScreenDestination(
                onContinue = {
                    navController.navigate(Home.route) {
                        popUpTo(Welcome.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(Home.route) {
            HomeScreen(
                worlds = PursaWorlds.all,
                onWorldClick = { worldId ->
                    navController.navigate(WorldDetail.createRoute(worldId))
                },
            )
        }

        composable(
            route = WorldDetail.route,
            arguments = listOf(
                navArgument(PursaRouteArgs.WorldId) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val worldId = backStackEntry.arguments?.getString(PursaRouteArgs.WorldId)
            val world = PursaWorlds.findById(worldId)

            if (world == null) {
                InvalidWorldScreen(
                    onBackToHome = {
                        navController.navigate(Home.route) {
                            popUpTo(Home.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                )
            } else {
                WorldDetailScreen(
                    world = world,
                    onBackClick = {
                        if (!navController.navigateUp()) {
                            navController.navigate(Home.route) {
                                launchSingleTop = true
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun WelcomeScreenDestination(onContinue: () -> Unit) {
    org.pursa.app.ui.welcome.WelcomeScreen(onPrimaryAction = onContinue)
}
