package com.hasan.jetfasthub.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.screens.LoginScreen

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Login : Screen("login", R.string.login)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun JetHubNavHost(
    darkTheme: Boolean
) {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = Screen.Login.route) {
        composable(
            Screen.Login.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(300))
            }
        ){
            LoginScreen(
                navController, darkTheme
            )
        }
    }
}