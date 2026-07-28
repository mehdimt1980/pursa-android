package org.pursa.app.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.pursa.app.content.data.StoryContentRepository
import org.pursa.app.feature.journal.JournalDetailScreen
import org.pursa.app.feature.journal.JournalDetailViewModel
import org.pursa.app.feature.journal.JournalListScreen
import org.pursa.app.feature.journal.JournalListViewModel
import org.pursa.app.feature.home.HomeScreen
import org.pursa.app.feature.home.PursaWorlds
import org.pursa.app.feature.missions.MissionListViewModel
import org.pursa.app.feature.settings.SettingsScreen
import org.pursa.app.feature.settings.SettingsViewModel
import org.pursa.app.feature.story.StoryRouteScreen
import org.pursa.app.feature.story.StoryLoadState
import org.pursa.app.feature.story.StoryViewModel
import org.pursa.app.feature.world.InvalidWorldScreen
import org.pursa.app.feature.world.WorldDetailScreen
import org.pursa.app.journal.data.ReflectionJournalRepository
import org.pursa.app.progress.data.MissionProgressRepository
import org.pursa.app.navigation.PursaDestination.Home
import org.pursa.app.navigation.PursaDestination.JournalDetail
import org.pursa.app.navigation.PursaDestination.JournalList
import org.pursa.app.navigation.PursaDestination.Settings
import org.pursa.app.navigation.PursaDestination.Story
import org.pursa.app.navigation.PursaDestination.Welcome
import org.pursa.app.navigation.PursaDestination.WorldDetail

@Composable
fun PursaNavGraph(
    storyRepository: StoryContentRepository,
    progressRepository: MissionProgressRepository,
    journalRepository: ReflectionJournalRepository,
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
                onSettingsClick = {
                    navController.navigate(Settings.route)
                },
                onJournalClick = {
                    navController.navigate(JournalList.route)
                },
            )
        }

        composable(JournalList.route) {
            val viewModel: JournalListViewModel = viewModel(
                factory = JournalListViewModel.factory(
                    journalRepository = journalRepository,
                    storyRepository = storyRepository,
                ),
            )
            val journalState by viewModel.state.collectAsStateWithLifecycle()
            JournalListScreen(
                state = journalState,
                onBackClick = {
                    if (!navController.navigateUp()) {
                        navController.navigate(Home.route) {
                            launchSingleTop = true
                        }
                    }
                },
                onHomeClick = {
                    navController.navigate(Home.route) {
                        popUpTo(Home.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onEntryClick = { storyId ->
                    navController.navigate(JournalDetail.createRoute(storyId))
                },
            )
        }

        composable(
            route = JournalDetail.route,
            arguments = listOf(
                navArgument(PursaRouteArgs.JournalStoryId) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val journalStoryId = backStackEntry.arguments?.getString(PursaRouteArgs.JournalStoryId).orEmpty()
            val viewModel: JournalDetailViewModel = viewModel(
                key = "journal-$journalStoryId",
                factory = JournalDetailViewModel.factory(
                    storyId = journalStoryId,
                    journalRepository = journalRepository,
                    storyRepository = storyRepository,
                ),
            )
            val journalState by viewModel.state.collectAsStateWithLifecycle()
            JournalDetailScreen(
                state = journalState,
                onBackClick = {
                    if (!navController.navigateUp()) {
                        navController.navigate(JournalList.route) {
                            launchSingleTop = true
                        }
                    }
                },
                onDeleteClick = viewModel::showDeleteDialog,
                onConfirmDelete = {
                    viewModel.deleteEntry {
                        navController.navigate(JournalList.route) {
                            popUpTo(JournalList.route) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                },
                onCancelDelete = viewModel::dismissDeleteDialog,
                onRetry = viewModel::load,
            )
        }

        composable(Settings.route) {
            val viewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.factory(progressRepository),
            )
            val settingsState by viewModel.state.collectAsStateWithLifecycle()
            SettingsScreen(
                state = settingsState,
                onBackClick = {
                    if (!navController.navigateUp()) {
                        navController.navigate(Home.route) {
                            launchSingleTop = true
                        }
                    }
                },
                onClearProgress = viewModel::clearProgress,
                onDismissClearDialog = viewModel::dismissDialog,
                onShowClearDialog = viewModel::showDialog,
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
                val viewModel: MissionListViewModel = viewModel(
                    key = "missions-${world.id}",
                    factory = MissionListViewModel.factory(
                        worldId = world.id,
                        storyRepository = storyRepository,
                        progressRepository = progressRepository,
                    ),
                )
                val missionListState by viewModel.state.collectAsStateWithLifecycle()

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
                        viewModel.retry()
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
            val viewModel: StoryViewModel = viewModel(
                key = "story-$storyId",
                factory = StoryViewModel.factory(
                    storyId = storyId,
                    storyRepository = storyRepository,
                    progressRepository = progressRepository,
                    journalRepository = journalRepository,
                ),
            )
            val storyState by viewModel.state.collectAsStateWithLifecycle()

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
                    viewModel.retry()
                },
                onReturnToWorld = {
                    val worldId = (storyState as? StoryLoadState.Success)?.story?.worldId ?: PursaWorlds.TruthId
                    if (!navController.popBackStack()) {
                        navController.navigate(WorldDetail.createRoute(worldId)) {
                            launchSingleTop = true
                        }
                    }
                },
                onSelectOption = viewModel::selectOption,
                onAdvance = viewModel::advance,
                onPrevious = viewModel::previous,
                onSelectJournalQuestion = viewModel::selectJournalQuestion,
                onSaveJournalEntry = viewModel::saveJournalEntry,
            )
        }
    }
}

@Composable
private fun WelcomeScreenDestination(onContinue: () -> Unit) {
    org.pursa.app.ui.welcome.WelcomeScreen(onPrimaryAction = onContinue)
}
