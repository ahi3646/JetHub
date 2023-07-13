package com.hasan.jetfasthub.screens.main.home.received_events_model

data class ReceivedEventsModelItem(
    val actor: Actor,
    val created_at: String,
    val id: String,
    val org: Org,
    val payload: Payload,
    val `public`: Boolean,
    val repo: Repo,
    val type: String
)