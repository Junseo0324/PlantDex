package com.devhjs.plantdex.presentation.text

import androidx.annotation.StringRes
import com.devhjs.plantdex.R
import com.devhjs.plantdex.domain.model.PlantLevel

@StringRes
fun PlantLevel.labelRes(): Int = when (this) {
    PlantLevel.SEEDLING -> R.string.level_seedling
    PlantLevel.OBSERVER -> R.string.level_observer
    PlantLevel.COLLECTOR -> R.string.level_collector
    PlantLevel.DOCTOR -> R.string.level_doctor
}
