package com.devhjs.plantdex.presentation.analyze

import androidx.annotation.StringRes
import com.devhjs.plantdex.R
import com.devhjs.plantdex.domain.model.Sunlight

@StringRes
fun Sunlight.labelRes(): Int = when (this) {
    Sunlight.FULL_SUN -> R.string.sunlight_full_sun
    Sunlight.BRIGHT_INDIRECT -> R.string.sunlight_bright_indirect
    Sunlight.PARTIAL_SHADE -> R.string.sunlight_partial_shade
    Sunlight.SHADE -> R.string.sunlight_shade
    Sunlight.UNKNOWN -> R.string.sunlight_unknown
}
