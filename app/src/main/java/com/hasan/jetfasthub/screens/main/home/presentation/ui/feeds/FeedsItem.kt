package com.hasan.jetfasthub.screens.main.home.presentation.ui.feeds

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.res.JetFastHubTheme
import com.hasan.jetfasthub.core.ui.res.JetHubTheme
import com.hasan.jetfasthub.core.ui.utils.Constants
import com.hasan.jetfasthub.core.ui.utils.ParseDateFormat
import com.hasan.jetfasthub.screens.main.home.domain.model.ReceivedEventsModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import java.util.Locale

@Composable
fun FeedsItem(
    eventItem: ReceivedEventsModel,
    onItemClick: (repoOwner: String, repoName: String) -> Unit
    //intentReducer: (HomeScreenClickIntents) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable {
                val uri = Uri.parse(eventItem.eventRepoUrl).lastPathSegment
                val parentUsername = Uri.parse(eventItem.eventRepoUrl).pathSegments[1]
                when (eventItem.eventType) {
                    "ForkEvent" -> {
                        onItemClick(
                            eventItem.eventActorLogin,
                            eventItem.eventPayloadForkeeName
                        )
//                        intentReducer(
//                            HomeScreenClickIntents.OpenRepositoryFragment(
//                                repoOwner = eventItem.eventActorLogin,
//                                repoName = eventItem.eventPayloadForkeeName
//                            )
//                        )
                    }

                    "ReleaseEvent" -> {
                        onItemClick(
                            parentUsername,
                            uri!!
                        )
//                        intentReducer(
//                            HomeScreenClickIntents.OpenRepositoryFragment(
//                                repoOwner = parentUsername,
//                                repoName = uri!!
//                            )
//                        )
                    }

                    else -> {
                        onItemClick(
                            parentUsername,
                            uri!!
                        )
//                        intentReducer(
//                            HomeScreenClickIntents.OpenRepositoryFragment(
//                                repoOwner = parentUsername,
//                                repoName = uri!!
//                            )
//                        )
                    }
                }
            },
        elevation = JetHubTheme.dimens.elevation0,
        backgroundColor = JetHubTheme.colors.background.primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = JetHubTheme.dimens.spacing8)
        ) {
            GlideImage(
                failure = { painterResource(id = R.drawable.baseline_account_circle_24) },
                imageModel = { eventItem.eventActorAvatarUrl },
                modifier = Modifier
                    .size(width = JetHubTheme.dimens.size48, height = JetHubTheme.dimens.size48)
                    .clip(CircleShape)
                    .clickable {
                        //TODO implement later
                        //intentReducer(HomeScreenClickIntents.OpenProfileFragment(eventItem.eventActorLogin))
                    },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.CenterStart,
                    contentDescription = null
                )
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = JetHubTheme.dimens.spacing12)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(eventItem.eventActorLogin)
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(
                                " " + stringResource(id = Constants.chooseFromEvents(eventItem.eventType).action).lowercase(
                                    Locale.getDefault()
                                ) + " "
                            )
                        }
                        append(eventItem.eventRepoName)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = JetHubTheme.colors.text.primary1,
                    style = JetHubTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.padding(top = JetHubTheme.dimens.spacing8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = Constants.chooseFromEvents(eventItem.eventType).icon),
                        contentDescription = stringResource(
                            id = Constants.chooseFromEvents(
                                eventItem.eventType
                            ).action
                        ),
                        tint = JetHubTheme.colors.icon.primary1
                    )
                    Text(
                        text = ParseDateFormat.getTimeAgo(eventItem.eventCreatedAt).toString(),
                        modifier = Modifier.padding(start = JetHubTheme.dimens.spacing8),
                        color = JetHubTheme.colors.text.secondary,
                        style = JetHubTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FeedsItem_LightPreview() {
    JetFastHubTheme(isDarkTheme = false) {
        FeedsItem(
            eventItem = ReceivedEventsModel(
                eventActorAvatarUrl = "",
                eventActorLogin = "Hasan",
                eventCreatedAt = "2001-20-03",
                eventPayloadForkeeName = "Forked",
                eventRepoName = "JetHub",
                eventRepoUrl = "",
                eventType = "CreateEvent",
                id = 1
            ),
            onItemClick = {_, _ -> }
            //intentReducer = { _ -> }
        )
    }
}

@Preview
@Composable
fun FeedsItem_DarkPreview() {
    JetFastHubTheme(isDarkTheme = true) {
        FeedsItem(
            eventItem = ReceivedEventsModel(
                eventActorAvatarUrl = "",
                eventActorLogin = "Hasan",
                eventCreatedAt = "2001-20-03",
                eventPayloadForkeeName = "Forked",
                eventRepoName = "JetHub",
                eventRepoUrl = "",
                eventType = "CreateEvent",
                id = 1
            ),
            onItemClick = {_, _ -> }
//            intentReducer = { _ -> }
        )
    }
}