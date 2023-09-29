package com.hasan.jetfasthub.core.ui.utils

object RepoQueryProvider {

    fun getIssuesPullRequestQuery(
        owner: String, repo: String,
        issueState: IssueState, isPr: Boolean
    ): String {
        return "+" + "type:" + (if (isPr) "pr" else "issue") +
                "+" + "repo:" + owner + "/" +
                repo + "+" + "is:" + issueState.name
    }

    fun getMyIssuesPullRequestQuery(
        username: String,
        issueState: IssueState,
        isPr: Boolean
    ): String {
        return "type:" + (if (isPr) "pr" else "issue") +
                "+" + "author:" + username +
                "+is:" + issueState.name
    }

    fun getAssigned(username: String, issueState: IssueState, isPr: Boolean): String {
        return "type:" + (if (isPr) "pr" else "issue") +
                "+" + "assignee:" + username +
                "+is:" + issueState.name
    }

    fun getMentioned(username: String, issueState: IssueState, isPr: Boolean): String {
        return "type:" + (if (isPr) "pr" else "issue") +
                "+" + "mentions:" + username +
                "+is:" + issueState.name
    }

    fun getReviewRequests(username: String, issueState: IssueState): String {
        return "type:pr" +
                "+" + "review-requested:" + username +
                "+is:" + issueState.name
    }

    fun getParticipated(username: String, issueState: IssueState, isPr: Boolean): String {
        return "type:" + (if (isPr) "pr" else "issue") +
                "+" + "involves:" + username +
                "+is:" + issueState.name
    }

}