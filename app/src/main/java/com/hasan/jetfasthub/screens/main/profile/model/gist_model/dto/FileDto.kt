package com.hasan.jetfasthub.screens.main.profile.model.gist_model.dto

import com.hasan.jetfasthub.screens.main.profile.model.gist_model.Files

data class FileDto(
    val filename: String,
    val language: String,
    val raw_url: String,
    val size: Int,
    val type: String
)

