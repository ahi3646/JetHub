package com.hasan.jetfasthub.screens.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.screens.login.LoginChooserClickIntents
import com.hasan.jetfasthub.screens.login.UserLoadCase

@Composable
fun LoginChooserScreen(
    state: UserLoadCase,
    intentReducer: (LoginChooserClickIntents) -> Unit,
    //TODO checkout modifier, they're not implemented rightly
    modifier: Modifier = Modifier,
) {
    when (state) {

        UserLoadCase.Nothing -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.white))
                    .padding(start = 30.dp, end = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Sign in using your GitHub account to use JetFastHub",
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Choose your type",
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { intentReducer(LoginChooserClickIntents.BasicAuthentication) }
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Blue.copy(.08f))
                ) {
                    Text(
                        text = "Basic Authentication", modifier = Modifier
                            .padding(18.dp, 12.dp, 18.dp, 12.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Blue,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // to do
                        }
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Blue.copy(.08f))
                ) {
                    Text(
                        text = "Access Token", modifier = Modifier
                            .padding(18.dp, 12.dp, 18.dp, 12.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Blue,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            //to do
                        }
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Blue.copy(.08f))
                ) {
                    Text(
                        text = "Enterprise", modifier = Modifier
                            .padding(18.dp, 12.dp, 18.dp, 12.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Blue,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                //OR
                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Red.copy(.08f))
                ) {
                    Text(
                        text = "OR", modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                IconButton(
                    onClick = { intentReducer(LoginChooserClickIntents.OAuthCLick) },
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Authenticate with google",
                        modifier = Modifier
                            .size(72.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }

        UserLoadCase.Fetching -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.white))
                    .padding(start = 30.dp, end = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Fetching user data, please wait a while :)",
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                CircularProgressIndicator()

            }
        }

        UserLoadCase.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.white))
                    .padding(start = 30.dp, end = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome",
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        UserLoadCase.Error -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.white))
                    .padding(start = 30.dp, end = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Something went wrong! Try to re sign in, please",
                    fontWeight = FontWeight.W400,
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

//@Preview
//@Composable
//private fun Preview_LoginChooserFragment_LightTheme() {
//    JetFastHubTheme(isDarkTheme = false) {
//        LoginChooserScreen(
//            basicOAuthClick = { /*TODO*/ },
//            state = UserLoadCase.Fetching
//        )
//    }
//}

@Preview
@Composable
private fun Preview_LoginChooserFragment_DarkTheme(@PreviewParameter(LoginChooserScreenStateProvide::class) state: UserLoadCase) {
    JetFastHubTheme(isDarkTheme = true) {
        LoginChooserScreen(
            state = state,
            intentReducer = {}
        )
    }
}

private class LoginChooserScreenStateProvide : CollectionPreviewParameterProvider<UserLoadCase>(
    collection = listOf(
        UserLoadCase.Nothing,
        UserLoadCase.Fetching,
        UserLoadCase.Success,
        UserLoadCase.Error,
    )
)