package com.hasan.jetfasthub.core.ui.utils

import com.hasan.jetfasthub.R

enum class IssueState {

    Open(R.string.opened),
    Closed(R.string.closed),
    All(R.string.all);

    constructor()

    constructor(status: Int){
        this.status = status
    }

    private var status: Int? = null

    fun getStatus(): Int? = status

}

