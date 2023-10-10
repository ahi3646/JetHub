package com.hasan.jetfasthub.core.ui.utils

import android.net.Uri
import com.hasan.jetfasthub.BuildConfig
import com.hasan.jetfasthub.core.ui.extensions.EventsType

object Constants {

    const val PERSONAL_ACCESS_TOKEN = BuildConfig.PERSONAL_ACCESS_TOKEN

    const val JetHubOwner = BuildConfig.JetHubOwner
    const val JetHubRepoName = BuildConfig.JetHubRepoName

    const val CLIENT_ID = BuildConfig.CLIENT_ID
    const val CLIENT_SECRET = BuildConfig.CLIENT_SECRET

    const val BASE_URL = BuildConfig.BASE_URL
    const val BASIC_AUTH_URL = BuildConfig.BASIC_AUTH_URL

    const val REDIRECT_URL = BuildConfig.REDIRECT_URL
    const val STATE = BuildConfig.STATE
    const val SCOPE = BuildConfig.SCOPE

    const val SHARED_PREF = BuildConfig.SHARED_PREF
    const val TOKEN_KEY = BuildConfig.TOKEN_KEY
    const val USERNAME_KEY = BuildConfig.USERNAME_KEY

    fun getAuthorizationUrl(): Uri {
        return Uri.Builder()
            .scheme("https")
            .authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", CLIENT_ID)
            .appendQueryParameter("redirect_uri", REDIRECT_URL)
            .appendQueryParameter("scope", SCOPE)
            .appendQueryParameter("state", STATE)
            .build()
    }

    fun chooseFromEvents(type: String): EventsType {
        return when (type) {
            "WatchEvent" -> {
                EventsType.WatchEvent
            }

            "CreateEvent" -> {
                EventsType.CreateEvent
            }

            "CommitCommentEvent" -> {
                EventsType.CommitCommentEvent
            }

            "DownloadEvent" -> {
                EventsType.DownloadEvent
            }

            "FollowEvent" -> {
                EventsType.FollowEvent
            }

            "ForkEvent" -> {
                EventsType.ForkEvent
            }

            "GistEvent" -> {
                EventsType.GistEvent
            }

            "GollumEvent" -> {
                EventsType.GollumEvent
            }

            "IssueCommentEvent" -> {
                EventsType.IssueCommentEvent
            }

            "IssuesEvent" -> {
                EventsType.IssuesEvent
            }

            "MemberEvent" -> {
                EventsType.MemberEvent
            }

            "PublicEvent" -> {
                EventsType.PublicEvent
            }

            "PullRequestEvent" -> {
                EventsType.PullRequestEvent
            }

            "PullRequestReviewCommentEvent" -> {
                EventsType.PullRequestReviewCommentEvent
            }

            "PullRequestReviewEvent" -> {
                EventsType.PullRequestReviewEvent
            }

            "RepositoryEvent" -> {
                EventsType.RepositoryEvent
            }

            "PushEvent" -> {
                EventsType.PushEvent
            }

            "TeamAddEvent" -> {
                EventsType.TeamAddEvent
            }

            "DeleteEvent" -> {
                EventsType.DeleteEvent
            }

            "ReleaseEvent" -> {
                EventsType.ReleaseEvent
            }

            "ForkApplyEvent" -> {
                EventsType.ForkApplyEvent
            }

            "OrgBlockEvent" -> {
                EventsType.OrgBlockEvent
            }

            "ProjectCardEvent" -> {
                EventsType.ProjectCardEvent
            }

            "ProjectColumnEvent" -> {
                EventsType.ProjectColumnEvent
            }

            "OrganizationEvent" -> {
                EventsType.OrganizationEvent
            }

            "ProjectEvent" -> {
                EventsType.ProjectEvent
            }

            else -> {
                EventsType.Undefined
            }
        }
    }
}

object NavigationConstants{

    const val GIST_DATA = "gist_data"
    const val ISSUE_OWNER = "issue_owner"
    const val ISSUE_REPO = "issue_repo"
    const val ISSUE_NUMBER = "issue_number"
    const val PROFILE_USERNAME = "username"
    const val PROFILE_START_TAB_INDEX = "start_index"
    const val REPOSITORY_OWNER = "repository_owner"
    const val REPOSITORY_NAME = "repository_name"

}
