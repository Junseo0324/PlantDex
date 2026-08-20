package com.devhjs.plantdex.presentation.profile

import androidx.compose.runtime.Stable
import com.devhjs.plantdex.domain.model.ProfileSummary

@Stable
data class ProfileState(
    val summary: ProfileSummary = ProfileSummary(),
)
