package org.pursa.app.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.pursa.app.content.data.StoryContentRepository
import org.pursa.app.content.data.StoryContentResult
import org.pursa.app.feature.home.HomeScreen
import org.pursa.app.feature.home.PursaWorlds
import org.pursa.app.feature.missions.MissionListUiState
import org.pursa.app.feature.story.StoryLoadState
import org.pursa.app.feature.story.StoryRouteScreen
import org.pursa.app.feature.world.InvalidWorldScreen
import org.pursa.app.feature.world.WorldDetailScreen
import org.pursa.app.navigation.PursaDestination.Home
import org.pursa.app.navigation.PursaDestination.Story
import org.pursa.app.navigation.PursaDestination.Welcome
import org.pursa.app.navigation.PursaDestination.WorldDetail

@Composable
fun PursaNavGraph(
    storyRepository: StoryContentRepository,
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
                var retryKey by remember(world.id) { mutableIntStateOf(0) }
                var missionListState by remember(world.id, retryKey) {
                    mutableStateOf<MissionListUiState>(MissionListUiState.Loading)
                }

                LaunchedEffect(world.id, retryKey) {
                    missionListState = when (val result = storyRepository.loadStoriesByWorld(world.id)) {
                        is StoryContentResult.InvalidContent -> MissionListUiState.InvalidContent
                        StoryContentResult.NotFound -> MissionListUiState.Success(emptyList())
                        is StoryContentResult.ReadFailure -> MissionListUiState.ReadFailure
                        is StoryContentResult.Success -> MissionListUiState.Success(result.value)
                    }
                }

                WorldDetailScreen(
                    world = world,
                    missionListState = missionListState,
                    onBackClick = {
                        if (!navController.navigateUp()) {
                            navController.navigate(Home.route) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onMissionClick = { storyId ->
                        navController.navigate(Story.createRoute(storyId))
                    },
                    onRetryMissions = {
                        retryKey += 1
                    },
                )
            }
        }

        composable(
            route = Story.route,
            arguments = listOf(
                navArgument(PursaRouteArgs.StoryId) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString(PursaRouteArgs.StoryId).orEmpty()
            var retryKey by remember(storyId) { mutableIntStateOf(0) }
            var storyState by remember(storyId, retryKey) {
                mutableStateOf<StoryLoadState>(StoryLoadState.Loading)
            }

            LaunchedEffect(storyId, retryKey) {
                storyState = when (val result = storyRepository.loadStory(storyId)) {
                    is StoryContentResult.InvalidContent -> StoryLoadState.InvalidContent
                    StoryContentResult.NotFound -> StoryLoadState.NotFound
                    is StoryContentResult.ReadFailure -> StoryLoadState.ReadFailure
                    is StoryContentResult.Success -> StoryLoadState.Success(result.value)
                }
            }

            StoryRouteScreen(
                state = storyState,
                onBackClick = {
                    if (!navController.navigateUp()) {
                        navController.navigate(Home.route) {
                            launchSingleTop = true
                        }
                    }
                },
                onRetry = {
                    retryKey += 1
                },
                onReturnToWorld = {
                    val worldId = (storyState as? StoryLoadState.Success)?.story?.worldId ?: PursaWorlds.TruthId
                    if (!navController.popBackStack()) {
                        navController.navigate(WorldDetail.createRoute(worldId)) {
                            launchSingleTop = true
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun WelcomeScreenDestination(onContinue: () -> Unit) {
    org.pursa.app.ui.welcome.WelcomeScreen(onPrimaryAction = onContinue)
}
