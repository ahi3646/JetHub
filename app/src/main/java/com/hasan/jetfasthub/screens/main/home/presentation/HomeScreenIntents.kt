package com.hasan.jetfasthub.screens.main.home.presentation

import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType
import com.hasan.jetfasthub.screens.main.home.presentation.state.config.bottom_bar.AppScreens

interface HomeScreenIntents {
    fun onRefreshSwipe()
    fun onIssuesStateChanged(type: MyIssuesType, state: IssueState)
    fun onIssueItemClick(issueOwner: String, issueRepo: String, issueNumber: String)
    fun onPullRequestsStateChanged(type: MyIssuesType, state: IssueState)
    fun onIssuesTabChange(tabIndex: Int)
    fun onPullsTabChange(tabIndex: Int)
    fun onDrawerTabChange(tabIndex: Int)
    fun onLogoutClick()
    fun onDismissBottomSheet()
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