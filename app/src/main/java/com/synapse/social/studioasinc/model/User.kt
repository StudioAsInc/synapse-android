package com.synapse.social.studioasinc.model

import com.google.firebase.database.PropertyName

data class User(
    @PropertyName("profileImageUrl")
    val profileImageUrl: String = ""
)
