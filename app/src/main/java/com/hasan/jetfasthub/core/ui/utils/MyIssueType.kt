package com.hasan.jetfasthub.core.ui.utils

import androidx.annotation.StringRes
import com.hasan.jetfasthub.R

enum class MyIssuesType(@StringRes val issueTypeName: Int) {
    CREATED(R.string.created_all_caps),
    ASSIGNED(R.string.assigned_all_caps),
    MENTIONED(R.string.mentioned_all_caps),
    REVIEW(R.string.review_requests_all_caps),
    PARTICIPATED(R.string.participated_all_caps)
}