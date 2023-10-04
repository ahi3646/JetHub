package com.hasan.jetfasthub.screens.main.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.core.ui.utils.IssueState

@Composable
fun TabItem(
    issuesCount: Int,
    tabIndex: Int,
    index: Int,
    isOpened: Boolean,
    tabName: String,
    onItemClick: () -> Unit,
    onStateChanged: (Int, IssueState) -> Unit,
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    Column(
        modifier = Modifier.onSizeChanged { itemHeight = with(density) { it.height.toDp() } }
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    onItemClick()
                    isContextMenuVisible = !isContextMenuVisible
                }
                .background(JetHubTheme.colors.background.secondary)
                .padding(JetHubTheme.dimens.spacing16)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_issue_opened_small),
                    tint = if (isOpened) colorResource(id = R.color.material_green_700) else colorResource(
                        id = R.color.material_red_700
                    ),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = JetHubTheme.dimens.spacing4),
                    text = "$tabName ($issuesCount)",
                    color = if (tabIndex == index) JetHubTheme.colors.text.primary1 else JetHubTheme.colors.text.secondary
                )
                Icon(
                    modifier = Modifier.padding(start = JetHubTheme.dimens.spacing4),
                    painter = painterResource(id = R.drawable.ic_dropdown_icon),
                    tint = if (tabIndex == index) JetHubTheme.colors.text.primary1 else JetHubTheme.colors.text.secondary,
                    contentDescription = null
                )
            }
            DropdownMenu(
                modifier = Modifier.background(color = JetHubTheme.colors.background.secondary),
                expanded = isContextMenuVisible,
                onDismissRequest = { isContextMenuVisible = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        onStateChanged(index, IssueState.Open)
                        isContextMenuVisible = false
                    },
                    content = {
                        Text(
                            text = stringResource(id = R.string.opened_upper),
                            color = JetHubTheme.colors.text.primary1
                        )
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        onStateChanged(index, IssueState.Closed)
                        isContextMenuVisible = false
                    },
                    content = {
                        Text(
                            text = stringResource(id = R.string.closed_upper),
                            color = JetHubTheme.colors.text.primary1
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun TabItem_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        TabItem(
            issuesCount = 100,
            tabIndex = 0,
            index = 0,
            isOpened = true,
            tabName = "Closed",
            onItemClick = { /*TODO*/ },
            onStateChanged = { _, _ -> }
        )
    }
}

@Preview
@Composable
fun TabItem_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        TabItem(
            issuesCount = 100,
            tabIndex = 0,
            index = 0,
            isOpened = true,
            tabName = "Closed",
            onItemClick = { /*TODO*/ },
            onStateChanged = { _, _ -> }
        )
    }
}
