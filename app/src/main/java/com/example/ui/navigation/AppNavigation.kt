package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.daily.DailyChallengeScreen
import com.example.ui.dictionary.DictionaryScreen
import com.example.ui.gameplay.GameScreen
import com.example.ui.gameplay.GameViewModel
import com.example.ui.home.HomeScreen
import com.example.ui.leaderboard.LeaderboardScreen
import com.example.ui.levels.LevelSelectScreen
import com.example.ui.stats.StatsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Game : Screen("game?levelId={levelId}&isDaily={isDaily}") {
        fun createRoute(levelId: Int, isDaily: Boolean = false) = "game?levelId=$levelId&isDaily=$isDaily"
    }
    object LevelSelect : Screen("level_select")
    object DailyChallenge : Screen("daily_challenge")
    object Dictionary : Screen("dictionary?word={word}") {
        fun createRoute(word: String? = null) = if (word != null) "dictionary?word=$word" else "dictionary?word="
    }
    object Leaderboard : Screen("leaderboard")
    object Stats : Screen("stats")
}

@Composable
fun AppNavigation(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) }
        ) {
            HomeScreen(
                viewModel = viewModel,
                onPlayLevel = { levelId ->
                    viewModel.loadLevel(levelId, isDaily = false)
                    navController.navigate(Screen.Game.createRoute(levelId, false))
                },
                onPlayDaily = {
                    navController.navigate(Screen.DailyChallenge.route)
                },
                onNavigateToLevels = {
                    navController.navigate(Screen.LevelSelect.route)
                },
                onNavigateToDictionary = {
                    navController.navigate(Screen.Dictionary.createRoute(null))
                },
                onNavigateToLeaderboard = {
                    navController.navigate(Screen.Leaderboard.route)
                },
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route)
                }
            )
        }

        composable(
            route = Screen.Game.route,
            arguments = listOf(
                navArgument("levelId") { type = NavType.IntType; defaultValue = 1 },
                navArgument("isDaily") { type = NavType.BoolType; defaultValue = false }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            val isDaily = backStackEntry.arguments?.getBoolean("isDaily") ?: false

            GameScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToLevels = {
                    navController.navigate(Screen.LevelSelect.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onNavigateToDictionary = { word ->
                    navController.navigate(Screen.Dictionary.createRoute(word))
                }
            )
        }

        composable(
            route = Screen.LevelSelect.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
        ) {
            LevelSelectScreen(
                viewModel = viewModel,
                onSelectLevel = { levelId ->
                    viewModel.loadLevel(levelId, isDaily = false)
                    navController.navigate(Screen.Game.createRoute(levelId, false))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.DailyChallenge.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
        ) {
            DailyChallengeScreen(
                viewModel = viewModel,
                onStartDailyGame = {
                    viewModel.loadLevel(999, isDaily = true)
                    navController.navigate(Screen.Game.createRoute(999, true))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Dictionary.route,
            arguments = listOf(
                navArgument("word") { type = NavType.StringType; defaultValue = ""; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
        ) { backStackEntry ->
            val word = backStackEntry.arguments?.getString("word")?.ifEmpty { null }
            DictionaryScreen(
                initialWord = word,
                onBack = { navController.popBackStack() },
                onPronounce = { viewModel.soundManager.playWordSuccess() }
            )
        }

        composable(
            route = Screen.Leaderboard.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
        ) {
            LeaderboardScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Stats.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
        ) {
            StatsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
