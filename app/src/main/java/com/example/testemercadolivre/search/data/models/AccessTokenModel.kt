package com.example.testemercadolivre.search.data.models

data class AccessTokenModel(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val scope: String,
    val token_type: String,
    val user_id: Int
)