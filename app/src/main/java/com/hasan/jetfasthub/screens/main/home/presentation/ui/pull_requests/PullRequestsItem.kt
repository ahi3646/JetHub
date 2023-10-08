package com.hasan.jetfasthub.screens.main.home.presentation.ui.pull_requests

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.core.ui.utils.ParseDateFormat
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesItem

@Composable
fun PullRequestsItem(
    modifier: Modifier = Modifier,
    elevation: Dp = JetHubTheme.dimens.elevation0,
    pull: IssuesItem,
//    intentReducer: (HomeScreenClickIntents) -> Unit
) {
    Card(
        elevation = elevation,
        backgroundColor = JetHubTheme.colors.background.primary,
        modifier = modifier
            .clickable(
                onClick = {
                    //onNavigate(R.id.action_fromFragment_to_issueFragment, null, null)
                }
            )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(all = JetHubTheme.dimens.spacing8)
        ) {
            Text(
                text = pull.title,
                modifier = Modifier.fillMaxWidth(),
                color = JetHubTheme.colors.text.primary1,
                style = JetHubTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            val repoUrl = Uri.parse(pull.repository_url).pathSegments
            val repoName = repoUrl[repoUrl.lastIndex - 1] + "/" + repoUrl[repoUrl.lastIndex]

            Row(modifier = Modifier.padding(top = JetHubTheme.dimens.spacing4)) {
                Text(
                    text = buildAnnotatedString {
                        append(repoName)
                        append("#${pull.number}")
                        append(" ")
                        if (pull.state == "closed") {
                            append(pull.state)
                            append(ParseDateFormat.getTimeAgo(pull.closed_at.toString()).toString())
                        } else {
                            append("${pull.state}ed")
                            append(ParseDateFormat.getTimeAgo(pull.created_at).toString())
                        }
                    },
                    color = JetHubTheme.colors.text.secondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
fun PullRequestsItem_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
//        PullRequestsItem(
//            pull = ,
//            onNavigate = {_, _, _ ->}
//        )
    }
}

@Preview
@Composable
fun PullRequestsItem_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
//        PullRequestsItem(
//            pull = ,
//            onNavigate = {_, _, _ ->}
//        )
    }
}