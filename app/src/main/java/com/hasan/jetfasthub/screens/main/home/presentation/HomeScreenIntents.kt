package com.hasan.jetfasthub.screens.main.home.presentation

import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.AppScreens

interface HomeScreenIntents {
    fun onRefreshSwipe()
    fun onIssuesStateChanged(issueState: IssueState, issuesType: MyIssuesType)
    fun onPullRequestsStateChanged(
        pullState: IssueState,
        issuesType: MyIssuesType
    )
    fun onDrawerClick()

    fun onTabChange(tabIndex: Int)
    fun onTabStateChange(state: IssueState)

    fun onLogoutClick()
    fun openProfileFragment(username: String, profileTabStartIndex: String = "0")
    fun onBottomBarItemClick(screen: AppScreens)
    fun openPinnedFragment()
    fun openGistsFragment(username: String)
    fun openRepositoryFragment(repoOwner: String, repoName: String)
    fun openFaqFragment()
    fun openSearchFragment()
    fun openIssueFragment(
        issueOwner: String,
        issueRepo: String,
        issueNumber: String
    )
    fun openSettingsFragment()
    fun openAboutFragment()
    fun openNotificationFragment()
}