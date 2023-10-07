package com.hasan.jetfasthub.core.ui.components

import androidx.compose.material.Text
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme

@Composable
fun CommonScrollableTab(
    modifier: Modifier = Modifier,
    containerColor: Color = JetHubTheme.colors.background.secondary,
    contentColor: Color = JetHubTheme.colors.text.primary1,
    edgePadding: Dp = JetHubTheme.dimens.spacing0,
    tabIndex: Int = 0,
    tabs: List<String>
) {
    var tabPosition by rememberSaveable { mutableIntStateOf(tabIndex) }
    ScrollableTabRow(
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier,
        edgePadding = edgePadding,
        selectedTabIndex = tabPosition
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = {
                    if (tabPosition == index) {
                        Text(
                            text = title,
                            color = JetHubTheme.colors.text.primary1,
                            style = JetHubTheme.typography.button
                        )
                    } else {
                        Text(
                            text = title,
                            color = JetHubTheme.colors.text.secondary,
                            style = JetHubTheme.typography.button
                        )
                    }
                },
                selected = tabPosition == index,
                onClick = { tabPosition = index },
                selectedContentColor = JetHubTheme.colors.text.primary1,
                unselectedContentColor = JetHubTheme.colors.text.tertiary,
            )
        }
    }
}

@Preview
@Composable
fun CommonScrollableTabRow_LightPreview(){
    JetFastHubTheme(isDarkTheme = false) {
        CommonScrollableTab(tabs = listOf("ASSIGNED", "CREATED", "OPENED", "MENTION"))
    }
}

@Preview
@Composable
fun CommonScrollableTabRow_DarkPreview(){
    JetFastHubTheme(isDarkTheme = true) {
        CommonScrollableTab(tabs = listOf("ASSIGNED", "CREATED", "OPENED", "MENTION"))
    }
}