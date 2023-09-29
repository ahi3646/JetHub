package com.hasan.jetfasthub.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme


@Composable
fun CommonJetHubLoginButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clickable { onClick() },
        content = {
            Text(
                text = text,
                modifier = Modifier
                    .padding(
                        horizontal = JetHubTheme.dimens.spacing16,
                        vertical = JetHubTheme.dimens.spacing16
                    )
                    .fillMaxWidth(),
                style = JetHubTheme.typography.subtitle2,
                color = JetHubTheme.colors.text.primary1,
                textAlign = TextAlign.Center
            )
        },
    )
}

@Preview
@Composable
private fun CommonJetHubLoginButton_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        CommonJetHubLoginButton(
            text = stringResource(id = R.string.basic_authentication),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(JetHubTheme.dimens.spacing12))
                .background(color = JetHubTheme.colors.background.primary),
        )
    }
}

@Preview
@Composable
private fun CommonJetHubLoginButton_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        CommonJetHubLoginButton(
            text = stringResource(id = R.string.basic_authentication),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(JetHubTheme.dimens.spacing12))
                .background(color = JetHubTheme.colors.background.primary),
        )
    }
}

