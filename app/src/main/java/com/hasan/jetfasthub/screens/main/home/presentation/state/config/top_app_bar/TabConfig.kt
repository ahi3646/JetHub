package com.hasan.jetfasthub.screens.main.home.presentation.state.config.top_app_bar

import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.TextReference
import com.hasan.jetfasthub.core.ui.utils.IssueState
import com.hasan.jetfasthub.core.ui.utils.MyIssuesType

sealed class HomeScreenTabConfig(val config: TabButtonConfig) {

    /** Lambda be invoked when tab button is clicked */
    abstract val onTabChange: (tabIndex: Int) -> Unit

    /** Lambda be invoked when dropdown tab button is clicked */
    abstract val onTabStateChange: (type: MyIssuesType, state: IssueState) -> Unit

    data class Created(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (type: MyIssuesType, state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 0,
            text = TextReference.Res(id = R.string.created_all_caps),
            onTabChange = onTabChange,
            state = IssueState.All,
            onTabStateChange = onTabStateChange,
            type = MyIssuesType.CREATED
        )
    )

    data class Assigned(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (type: MyIssuesType, state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 1,
            text = TextReference.Res(id = R.string.assigned_all_caps),
            onTabChange = onTabChange,
            state = IssueState.All,
            onTabStateChange = onTabStateChange,
            type = MyIssuesType.ASSIGNED
        )
    )

    data class Mentioned(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (type: MyIssuesType, state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 1,
            text = TextReference.Res(id = R.string.mentioned_all_caps),
            onTabChange = onTabChange,
            state = IssueState.All,
            onTabStateChange = onTabStateChange,
            type = MyIssuesType.MENTIONED
        )
    )

    data class Participated(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (type: MyIssuesType, state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 2,
            text = TextReference.Res(id = R.string.participated_all_caps),
            onTabChange = onTabChange,
            state = IssueState.All,
            onTabStateChange = onTabStateChange,
            type = MyIssuesType.PARTICIPATED
        )
    )

    data class ReviewRequest(
        override val onTabChange: (tabIndex: Int) -> Unit,
        override val onTabStateChange: (type: MyIssuesType, state: IssueState) -> Unit
    ) : HomeScreenTabConfig(
        config = TabButtonConfig(
            tabIndex = 3,
            text = TextReference.Res(id = R.string.review_requests_all_caps),
            onTabChange = onTabChange,
            onTabStateChange = onTabStateChange,
            state = IssueState.All,
            type = MyIssuesType.REVIEW
        )
    )
}


data class TabButtonConfig(
    val tabIndex: Int,
    val text: TextReference,
    var state: IssueState,
    val type: MyIssuesType,
    val onTabChange: (tabIndex: Int) -> Unit,
    val onTabStateChange: (type: MyIssuesType, state: IssueState) -> Unit
)

sealed class DrawerTabConfig(val config: DrawerTabButtonConfig) {
    /** Lambda be invoked when tab button is clicked */
    abstract val onTabChange: (tabIndex: Int) -> Unit

    data class Menu(override val onTabChange: (tabIndex: Int) -> Unit) : DrawerTabConfig(
        config = DrawerTabButtonConfig(
            tabIndex = 0,
            text = TextReference.Res(id = R.string.menu_all_caps),
            onTabChange = onTabChange
        )
    )
    data class Profile(override val onTabChange: (tabIndex: Int) -> Unit) : DrawerTabConfig(
        config = DrawerTabButtonConfig(
            tabIndex = 1,
            text = TextReference.Res(id = R.string.profile_all_caps),
            onTabChange = onTabChange
        )
    )
}

data class DrawerTabButtonConfig(
    val tabIndex: Int,
    val text: TextReference,
    val onTabChange: (tabIndex: Int) -> Unit,
)