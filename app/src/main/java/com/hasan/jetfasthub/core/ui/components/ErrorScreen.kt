package com.hasan.jetfasthub.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String = stringResource(id = R.string.could_not_load_data)
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = JetHubTheme.colors.background.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage,
            color = JetHubTheme.colors.text.primary1,
            style = JetHubTheme.typography.button
        )
    }
}

@Preview
@Composable
fun ErrorScreen_LightPreview(){
    JetFastHubTheme(isDarkTheme = false) {
        ErrorScreen()
    }
}

@Preview
@Composable
fun ErrorScreen_DarkPreview(){
    JetFastHubTheme(isDarkTheme = true) {
        ErrorScreen()
    }
}