package com.hasan.jetfasthub.screens.main.home.presentation

sealed interface HomeScreenNavigation {
    data object OpenNotificationFragment : HomeScreenNavigation
    data object OpenPinnedFragment : HomeScreenNavigation
    data class OpenProfileFragment(val username: String, val profileTabStartIndex: String = "0") : HomeScreenNavigation
    data class OpenRepositoryFragment(val repoOwner: String, val repoName: String) : HomeScreenNavigation
    data object OpenSearchFragment : HomeScreenNavigation
    data object OpenSettingsFragment : HomeScreenNavigation
    data object OpenAboutFragment : HomeScreenNavigation
    data object OpenFaqFragment : HomeScreenNavigation
    data class OpenGistsFragment(val username: String) : HomeScreenNavigation
    data class OpenIssueFragmentFragment(
        val issueOwner: String,
        val issueRepo: String,
        val issueNumber: String
    ) : HomeScreenNavigation
}
