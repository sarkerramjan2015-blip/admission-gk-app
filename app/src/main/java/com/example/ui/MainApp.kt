package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.firebase.AuthManager
import com.example.ui.navigation.*
import com.example.ui.screens.*

@Composable
fun MainApp(viewModel: GKViewModel, authManager: AuthManager, fingerprintAvailable: Boolean, onFingerprintLogin: () -> Unit) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(navController = navController, startDestination = SplashRoute) {
            composable<SplashRoute> {
                SplashScreen(onTimeout = {
                    if (authManager.isLoggedIn) {
                        navController.navigate(HomeRoute) {
                            popUpTo<SplashRoute> { inclusive = true }
                        }
                    } else {
                        navController.navigate(LoginRoute) {
                            popUpTo<SplashRoute> { inclusive = true }
                        }
                    }
                })
            }

            composable<LoginRoute> {
                LoginScreen(
                    onGoogleSignIn = {
                        authManager.login()
                        navController.navigate(HomeRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onFingerprintLogin = onFingerprintLogin,
                    fingerprintAvailable = fingerprintAvailable,
                    isLoading = false
                )
            }

            composable<HomeRoute> {
                HomeScreen(viewModel, navController)
            }

            composable<CategoryRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<CategoryRoute>()
                CategoryScreen(route.category, route.title, viewModel, navController)
            }

            composable<TopicListRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<TopicListRoute>()
                TopicListScreen(route.topicId, route.title, viewModel, navController)
            }

            composable<SubTopicDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<SubTopicDetailRoute>()
                SubTopicDetailScreen(route.subTopicId, viewModel, navController)
            }

            composable<MCQQuizRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<MCQQuizRoute>()
                MCQQuizIntroScreen(route.subTopicId, viewModel, navController)
            }

            composable<MCQQuizLiveRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<MCQQuizLiveRoute>()
                MCQQuizLiveScreen(route.subTopicId, viewModel, navController)
            }

            composable<MCQPracticeRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<MCQPracticeRoute>()
                MCQPracticeScreen(route.subTopicId, viewModel, navController)
            }

            composable<MegaQuizIntroRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<MegaQuizIntroRoute>()
                MegaQuizIntroScreen(route.examId, viewModel, navController)
            }

            composable<MegaQuizLiveRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<MegaQuizLiveRoute>()
                MegaQuizLiveScreen(route.examId, viewModel, navController)
            }

            composable<MCQQuizResultRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<MCQQuizResultRoute>()
                MCQQuizResultScreen(route.subTopicId, route.score, route.total, route.correct, route.wrong, route.timeTakenSeconds, navController)
            }

            composable<MegaQuizSubmittedRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<MegaQuizSubmittedRoute>()
                MegaQuizSubmittedScreen(route.total, route.answered, route.unanswered, navController)
            }

            composable<MegaQuizResultRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<MegaQuizResultRoute>()
                MegaQuizResultScreen(route.score, route.total, route.right, route.wrong, navController)
            }

            composable<QuestionBankRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<QuestionBankRoute>()
                QuestionBankScreen(route.uniId, route.year, viewModel, navController)
            }

            composable<RecentGKRoute> {
                GKFeedScreen(viewModel, navController)
            }

            composable<ProfileRoute> {
                ProfileScreen(viewModel, navController, onSignOut = {
                    authManager.logout()
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                })
            }

            composable<AdminDashboardRoute> {
                AdminDashboardScreen(viewModel, navController)
            }

            composable<MegaQuizRoutineRoute> {
                MegaQuizRoutineScreen(viewModel, navController)
            }

            composable<ProgressReportRoute> {
                ProgressReportScreen(viewModel, navController)
            }
        }
    }
}
