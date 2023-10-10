package com.hasan.jetfasthub.core.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.hasan.jetfasthub.core.ui.res.JetHubTheme

@Composable
fun CommonJetHubLoginTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next, // ** Done. Close the keyboard **
        keyboardType = KeyboardType.Text
    ),
    shape: Shape = JetHubTheme.shapes.roundedCornersMedium,
    onTextChanged: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        colors = TextFieldDefaults.textFieldColors(
            textColor = JetHubTheme.colors.text.primary1,
            backgroundColor = JetHubTheme.colors.background.primary,
            cursorColor = JetHubTheme.colors.text.primary1,
            disabledLabelColor = Color.Red,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = label,
                color = JetHubTheme.colors.text.primary1
            )
        },
        onValueChange = {
            onTextChanged(it)
        },
        keyboardOptions = keyboardOptions,
        shape = shape,
        singleLine = true,
        trailingIcon = {
//                if (textState.isNotEmpty()) {
//                    IconButton(onClick = { textState = "" }) {
//                        Icon(
//                            imageVector = Icons.Outlined.Close,
//                            contentDescription = null
//                        )
//                    }
//                }
        }
    )
}