package com.hasan.jetfasthub.screens.login.presentation.basicAuth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.components.CommonJetHubLoginButton
import com.hasan.jetfasthub.core.ui.components.CommonJetHubLoginTextField
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme

/**
 * BasicAuth screen
 *
 * @param state screen state
 * @param modifier screen modifier
 * @param intentReducer generic class to handle all intents
 *
 * @author Anorov Hasan on 30/09/2023
 */

@Composable
fun BasicAuthScreen(
    modifier: Modifier = Modifier,
    state: BasicAuthUiState,
    intentReducer: (BasicAuthClickIntents) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonJetHubLoginTextField(
            value = state.userName,
            label = stringResource(id = R.string.username),
            onTextChanged = { intentReducer(BasicAuthClickIntents.OnUsernameChanged(it)) },
            shape = RoundedCornerShape(
                topStart = JetHubTheme.dimens.radius12,
                topEnd = JetHubTheme.dimens.radius12
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetHubTheme.dimens.spacing16)
        )
        CommonJetHubLoginTextField(
            value = state.password,
            label = stringResource(id = R.string.password),
            onTextChanged = { intentReducer(BasicAuthClickIntents.OnPasswordChanged(it)) },
            shape = RoundedCornerShape(
                bottomStart = JetHubTheme.dimens.radius12,
                bottomEnd = JetHubTheme.dimens.radius12
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, // ** Done. Close the keyboard **
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetHubTheme.dimens.spacing16)
        )
        CommonJetHubLoginButton(
            onClick = { intentReducer(BasicAuthClickIntents.LoginClick) },
            text = stringResource(id = R.string.login),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetHubTheme.dimens.spacing16)
                .padding(top = JetHubTheme.dimens.spacing24)
                .clip(RoundedCornerShape(JetHubTheme.dimens.spacing12))
                .background(color = JetHubTheme.colors.background.primary)
        )
        Box(
            modifier = Modifier
                .padding(top = JetHubTheme.dimens.spacing24)
                .clip(RoundedCornerShape(size = JetHubTheme.dimens.radius8))
                .background(Color.Red.copy(.08f)),
            content = {
                Text(
                    text = stringResource(id = R.string.or_with),
                    modifier = Modifier.padding(
                        horizontal = JetHubTheme.dimens.spacing12,
                        vertical = JetHubTheme.dimens.spacing6
                    ),
                    style = JetHubTheme.typography.caption.copy(fontWeight = FontWeight.Medium),
                    color = Color.Red
                )
            }
        )
        CommonJetHubLoginButton(
            onClick = { intentReducer(BasicAuthClickIntents.BrowserAuth) },
            text = stringResource(id = R.string.browser_login),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = JetHubTheme.dimens.spacing24)
                .padding(horizontal = JetHubTheme.dimens.spacing16)
                .clip(RoundedCornerShape(JetHubTheme.dimens.spacing12))
                .background(color = JetHubTheme.colors.background.primary)
        )
        CommonJetHubLoginButton(
            onClick = { intentReducer(BasicAuthClickIntents.GoPreviousScreen) },
            text = stringResource(id = R.string.previous_screen),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = JetHubTheme.dimens.spacing12)
                .padding(horizontal = JetHubTheme.dimens.spacing16)
                .clip(RoundedCornerShape(JetHubTheme.dimens.spacing12))
                .background(color = JetHubTheme.colors.background.primary)
        )
    }
}

@Preview
@Composable
fun BasicAuthScreen_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        BasicAuthScreen(
            state = BasicAuthUiState(),
            intentReducer = { },
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.secondary)
        )
    }
}

@Preview
@Composable
fun BasicAuthScreen_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        BasicAuthScreen(
            state = BasicAuthUiState(),
            intentReducer = { },
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.secondary)
        )
    }
}

