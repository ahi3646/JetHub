package com.hasan.jetfasthub.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.ui.theme.JetFastHubTheme

@Composable
fun LoginScreen(
    navController: NavController,
    darkTheme: Boolean
) {

    JetFastHubTheme {
        Column(
            modifier = Modifier
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

            val types = arrayOf(
                "Basic Authentication",
                "Access Token",
                "Enterprise"
            )

            for (type in types) {
                LoginType(type = type)
                Spacer(modifier = Modifier.height(16.dp))
            }

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

            IconButton(onClick = {

            }, modifier = Modifier.padding(12.dp)) {
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
}

@Composable
fun LoginType(type: String) {
    val color = Color.Blue
    ChipView(type = type, color)
}

@Composable
fun ChipView(type: String, colorResource: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource.copy(.08f))
    ) {
        Text(
            text = type, modifier = Modifier
                .padding(18.dp, 12.dp, 18.dp, 12.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
            color = colorResource,
            textAlign = TextAlign.Center
        )
    }
}
