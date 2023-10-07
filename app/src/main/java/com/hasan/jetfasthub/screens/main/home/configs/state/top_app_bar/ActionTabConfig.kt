package com.hasan.jetfasthub.screens.main.home.configs.state.top_app_bar

import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.TextReference
import com.hasan.jetfasthub.core.ui.utils.IssueState

sealed class HomeScreenTabConfig(val config: TabButtonConfig) {

    /** Lambda be invoked when manage button is clicked */
    abstract val onTabChange: (tabIndex: Int) -> Unit
    abstract val onTabStateChange: (state: IssueState) -> Unit

    data class Created(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 0,
            text = TextReference.Res(id = R.string.created_all_caps),
            onTabChange = onTabChange,
            state = IssueState.All,
            onTabStateChange = onTabStateChange
        )
    )

    data class Assigned(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 1,
            text = TextReference.Res(id = R.string.assigned_all_caps),
            onTabChange = onTabChange,
            state = IssueState.All,
            onTabStateChange = onTabStateChange
        )
    )

    data class Mentioned(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 1,
            text = TextReference.Res(id = R.string.mentioned_all_caps),
            onTabChange = onTabChange,
            state = IssueState.All,
            onTabStateChange = onTabStateChange
        )
    )

    data class Participated(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 2,
            text = TextReference.Res(id = R.string.participated_all_caps),
            onTabChange = onTabChange,
            state = IssueState.All,
            onTabStateChange = onTabStateChange
        )
    )

    data class ReviewRequest(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 3,
            text = TextReference.Res(id = R.string.review_requests_all_caps),
            onTabChange = onTabChange,
            onTabStateChange = onTabStateChange,
            state = IssueState.All
        )
    )
}


data class TabButtonConfig(
    val tabIndex: Int,
    val text: TextReference,
    val state: IssueState,
    val onTabChange: (tabIndex: Int) -> Unit,
    val onTabStateChange: (state: IssueState) -> Unit
)