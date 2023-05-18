package com.hasan.jetfasthub.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.hasan.jetfasthub.MainActivity
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.networking.GitHubHelper
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.networking.model.AccessTokenModel
import com.hasan.jetfasthub.networking.model.AuthModel
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme
import kotlinx.coroutines.launch
import okio.ByteString.Companion.encode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicLoginScreen(
    navController: NavController, darkTheme: Boolean, context: Context
) {

    val userName = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    val passwordVisibility = remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    JetFastHubTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.white))
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = userName.value,
                onValueChange = { userName.value = it },
                label = { Text(text = "Username", color = Color.Blue) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Blue
                ),
                placeholder = { Text(text = "Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_remove_red_eye_24),
                            contentDescription = "See password",
                            tint = if (passwordVisibility.value) MaterialTheme.colorScheme.primary
                            else Color.Blue.copy(.6f)
                        )
                    }
                },
                label = {
                    Text(
                        text = "Password",
                        color = Color.Blue
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Blue
                ),
                visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                placeholder = { Text(text = "Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {

                            val authModel = AuthModel()

                            /**
                             * Later move this logic to viewModel
                             *
                            RetrofitInstance(context).api
                            .login(
                            authModel,
                            )
                            .enqueue(object : Callback<AccessTokenModel> {
                            override fun onResponse(
                            call: Call<AccessTokenModel>,
                            response: Response<AccessTokenModel>
                            ) {
                            Log.d(
                            "ahi3646", "onResponse: ${
                            response
                            .body()
                            .toString()
                            }"
                            )
                            }

                            override fun onFailure(
                            call: Call<AccessTokenModel>,
                            t: Throwable
                            ) {
                            Log.d(
                            "ahi3646", "onFailure: ${
                            t.stackTrace
                            } "
                            )
                            }

                            })

                             */
                        }
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Blue.copy(.08f))
            ) {
                Text(
                    text = "Login", modifier = Modifier
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

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, getAuthorizationUrl())
                        context.startActivity(intent)
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Blue.copy(.08f))
            ) {
                Text(
                    text = "Sign in with your default browser", modifier = Modifier
                        .padding(18.dp, 12.dp, 18.dp, 12.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Blue,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

fun getAuthorizationUrl(): Uri {
    return Uri.Builder()
        .scheme("https")
        .authority("github.com")
        .appendPath("login")
        .appendPath("oauth")
        .appendPath("authorize")
        .appendQueryParameter("client_id", GitHubHelper.CLIENT_ID)
        .appendQueryParameter("redirect_uri", GitHubHelper.REDIRECT_URL)
        .appendQueryParameter("scope", GitHubHelper.SCOPE)
        .appendQueryParameter("state", GitHubHelper.STATE)
        .build()
}