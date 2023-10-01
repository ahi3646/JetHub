package com.hasan.jetfasthub.screens.login.presentation.loginPage

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.components.CommonJetHubLoginButton
import com.hasan.jetfasthub.core.ui.extensions.resolveReference
import com.hasan.jetfasthub.core.ui.extensions.resourceReference
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.screens.login.presentation.LoginPageState

/**
 * LoginChooserScreen screen
 *
 * @param state screen state
 * @param modifier screen modifier
 * @param intentReducer generic class to handle all intents
 *
 * @author Anorov Hasan on 30/09/2023
 */

@Composable
fun LoginChooserScreen(
    state: LoginPageState,
    intentReducer: (LoginChooserClickIntents) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        LoginPageState.Default -> LoginChooserDefaultContent(
            modifier = modifier,
            intentReducer = intentReducer
        )

        LoginPageState.Fetching -> LoginChooserFetchingContent(modifier = modifier)
        LoginPageState.Success -> LoginChooserSuccessContent(modifier = modifier)
        is LoginPageState.Error -> {
            Toast.makeText(
                LocalContext.current,
                state.errorMessage.resolveReference(),
                Toast.LENGTH_SHORT
            ).show()
            LoginChooserErrorContent(modifier = modifier)
        }
    }
}

@Composable
private fun LoginChooserDefaultContent(
    modifier: Modifier = Modifier,
    intentReducer: (LoginChooserClickIntents) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(horizontal = JetHubTheme.dimens.spacing24),
            text = stringResource(id = R.string.usage_guide),
            color = JetHubTheme.colors.text.primary1,
            style = JetHubTheme.typography.h3,
            textAlign = TextAlign.Center
        )

        CommonJetHubLoginButton(
            onClick = {
                intentReducer(LoginChooserClickIntents.BasicAuthentication)
            },
            text = stringResource(id = R.string.basic_authentication),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetHubTheme.dimens.spacing16)
                .padding(top = JetHubTheme.dimens.spacing24)
                .clip(RoundedCornerShape(JetHubTheme.dimens.spacing12))
                .background(color = JetHubTheme.colors.background.primary),
        )
        CommonJetHubLoginButton(
            onClick = { },
            text = stringResource(id = R.string.access_token),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetHubTheme.dimens.spacing16)
                .padding(top = JetHubTheme.dimens.spacing8)
                .clip(RoundedCornerShape(JetHubTheme.dimens.spacing12))
                .background(color = JetHubTheme.colors.background.primary),
        )
        CommonJetHubLoginButton(
            onClick = { },
            text = stringResource(id = R.string.enterprise),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = JetHubTheme.dimens.spacing16)
                .padding(top = JetHubTheme.dimens.spacing8)
                .clip(RoundedCornerShape(JetHubTheme.dimens.spacing12))
                .background(color = JetHubTheme.colors.background.primary),
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
        IconButton(
            onClick = { intentReducer(LoginChooserClickIntents.OAuthCLick) },
            modifier = Modifier
                .padding(JetHubTheme.dimens.spacing16)
                .clip(RoundedCornerShape(JetHubTheme.dimens.radius24))
                .background(JetHubTheme.colors.background.primary),
            content = {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Authenticate with google",
                )
            }
        )
    }
}

@Composable
private fun LoginChooserFetchingContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.fetching_status),
            color = JetHubTheme.colors.text.primary1,
            style = JetHubTheme.typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = JetHubTheme.dimens.spacing16)
        )
        CircularProgressIndicator(
            color = JetHubTheme.colors.stroke.secondary,
            modifier = Modifier.padding(top = JetHubTheme.dimens.spacing24)
        )
    }
}

@Composable
private fun LoginChooserSuccessContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome),
            color = JetHubTheme.colors.text.primary1,
            style = JetHubTheme.typography.h4,
        )
    }
}

@Composable
private fun LoginChooserErrorContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.login_status_error),
            color = JetHubTheme.colors.text.primary1,
            style = JetHubTheme.typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(JetHubTheme.dimens.spacing16)
        )
    }
}

@Preview(locale = "ru")
@Composable
private fun Preview_LoginChooserFragment_LightTheme(@PreviewParameter(LoginChooserScreenStateProvide::class) state: LoginPageState) {
    JetFastHubTheme(isDarkTheme = false) {
        LoginChooserScreen(
            state = state,
            intentReducer = {},
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.secondary)
        )
    }
}

@Preview(locale = "ru")
@Composable
private fun Preview_LoginChooserFragment_DarkTheme(@PreviewParameter(LoginChooserScreenStateProvide::class) state: LoginPageState) {
    JetFastHubTheme(isDarkTheme = true) {
        LoginChooserScreen(
            state = state,
            intentReducer = {},
            modifier = Modifier
                .fillMaxSize()
                .background(color = JetHubTheme.colors.background.secondary)
        )
    }
}

private class LoginChooserScreenStateProvide : CollectionPreviewParameterProvider<LoginPageState>(
    collection = listOf(
        LoginPageState.Default,
        LoginPageState.Fetching,
        LoginPageState.Success,
        LoginPageState.Error(resourceReference(R.string.login_unsuccessful_response)),
    )
)
