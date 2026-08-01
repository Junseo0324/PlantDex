package com.devhjs.plantdex.presentation.analyze

import androidx.annotation.StringRes
import com.devhjs.plantdex.R
import com.devhjs.plantdex.domain.model.AnalysisError

@StringRes
fun AnalysisError.titleRes(): Int = when (this) {
    AnalysisError.NotAPlant -> R.string.error_not_a_plant_title
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
    AnalysisError.Network -> R.string.error_network_body
    AnalysisError.Timeout,
    AnalysisError.QuotaExceeded,
    AnalysisError.MalformedResponse,
    is AnalysisError.Unknown,
        -> R.string.error_generic_body
}
