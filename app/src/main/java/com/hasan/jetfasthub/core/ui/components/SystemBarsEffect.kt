package com.hasan.jetfasthub.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/** Usage:@
  val statusBarColor = TangemTheme.colors.background.secondary
  SystemBarsEffect {
      setSystemBarsColor(color = statusBarColor)
  }
 */

@Composable
fun SystemBarsEffect(block: SystemUiController.() -> Unit) {
    val systemUiController = rememberSystemUiController()
    SideEffect { block(systemUiController) }
}
