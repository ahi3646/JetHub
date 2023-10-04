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
fun EmptyScreen(
    modifier: Modifier = Modifier,
    message: String = stringResource(id = R.string.no_data_to_load)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = JetHubTheme.colors.text.primary1,
            style = JetHubTheme.typography.button
        )
    }
}

@Preview
@Composable
fun EmptyScreen_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        EmptyScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.primary)
        )
    }
}

@Preview
@Composable
fun EmptyScreen_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        EmptyScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.primary)
        )
    }
}