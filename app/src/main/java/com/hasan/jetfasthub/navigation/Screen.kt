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
import com.hasan.jetfasthub.screens.login.LoginChooserFragment
import com.hasan.jetfasthub.screens.login.basic_auth.BasicAuthFragment

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object LoginChooser : Screen("login_chooser", R.string.login)
    object BasicLogin : Screen("basic_auth", R.string.basic_auth)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginNavHost(){
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController, startDestination = Screen.LoginChooser.route) {
        composable(
            Screen.LoginChooser.route,
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
            LoginChooserFragment()
        }

        composable(
            Screen.BasicLogin.route,
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
            BasicAuthFragment()
        }
    }
}