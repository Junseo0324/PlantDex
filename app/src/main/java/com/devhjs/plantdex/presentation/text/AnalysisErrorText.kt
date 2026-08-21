package com.devhjs.plantdex.presentation.text

import androidx.annotation.StringRes
import com.devhjs.plantdex.R
import com.devhjs.plantdex.domain.model.AnalysisError

// analyze error text
@StringRes
fun AnalysisError.titleRes(): Int = when (this) {
    AnalysisError.NotAPlant -> R.string.error_not_a_plant_title
    AnalysisError.PhotoUnavailable -> R.string.error_photo_unavailable_title
    AnalysisError.Network -> R.string.error_network_title
    AnalysisError.Timeout,
    AnalysisError.QuotaExceeded,
    AnalysisError.MalformedResponse,
    is AnalysisError.Unknown,
        -> R.string.error_generic_title
}

@StringRes
fun AnalysisError.bodyRes(): Int = when (this) {
    AnalysisError.NotAPlant -> R.string.error_not_a_plant_body
    AnalysisError.PhotoUnavailable -> R.string.error_photo_unavailable_body
    AnalysisError.Network -> R.string.error_network_body
    AnalysisError.Timeout,
    AnalysisError.QuotaExceeded,
    AnalysisError.MalformedResponse,
    is AnalysisError.Unknown,
        -> R.string.error_generic_body
}
