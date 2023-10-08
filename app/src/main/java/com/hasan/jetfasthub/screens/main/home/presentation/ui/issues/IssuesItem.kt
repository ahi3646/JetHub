package com.hasan.jetfasthub.screens.main.home.presentation.ui.issues

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.core.ui.utils.ParseDateFormat
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesItem

@Composable
fun IssuesItemCard(
    issue: IssuesItem,
    modifier: Modifier = Modifier,
    onIssueItemClicked: (String, String, String) -> Unit
) {
    val repoUrl = Uri.parse(issue.repository_url).pathSegments
    val repoName = repoUrl[repoUrl.lastIndex - 1] + "/" + repoUrl[repoUrl.lastIndex]
    Card(
        elevation = JetHubTheme.dimens.elevation0,
        backgroundColor = JetHubTheme.colors.background.primary,
        modifier = modifier
            .clickable(
                onClick = {
                    onIssueItemClicked(
                        repoUrl[repoUrl.lastIndex - 1],
                        repoUrl[repoUrl.lastIndex],
                        issue.number.toString(),
                    )
                }
            )
    ) {
        Column(
            modifier = Modifier.padding(all = JetHubTheme.dimens.spacing8),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = issue.title,
                modifier = Modifier
                    .fillMaxWidth(),
                color = JetHubTheme.colors.text.primary1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = JetHubTheme.dimens.spacing4)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(repoName)
                        append(" ")
                        append("#${issue.number}")
                        append(" ")
                        append(issue.state)
                        append(" ")
                        if (issue.closed_at != null) {
                            append(
                                ParseDateFormat.getTimeAgo(issue.closed_at.toString()).toString()
                            )
                        }
                    },
                    color = JetHubTheme.colors.text.secondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )

                if (issue.comments != 0) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_comment_small),
                        modifier = Modifier.padding(horizontal = JetHubTheme.dimens.spacing4),
                        tint = JetHubTheme.colors.icon.primary1,
                        contentDescription = null
                    )
                    Text(
                        text = issue.comments.toString(),
                        color = JetHubTheme.colors.text.secondary,
                        modifier = Modifier.padding(start = JetHubTheme.dimens.spacing4)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun IssuesItem_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
//        IssuesItemCard(
//            issue = ,
//            onIssueItemClicked = { _, _, _, _ -> }
//        )
    }
}

@Preview
@Composable
fun IssuesItem_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
//        IssuesItemCard(
//            //issue = ,
//            onIssueItemClicked = { _, _, _, _ -> }
//        )
    }
}

