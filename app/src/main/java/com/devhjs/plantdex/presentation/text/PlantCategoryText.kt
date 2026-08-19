package com.devhjs.plantdex.presentation.text

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.devhjs.plantdex.R
import com.devhjs.plantdex.domain.model.PlantCategory
import com.devhjs.plantdex.presentation.designsystem.AppColors

@StringRes
fun PlantCategory.labelRes(): Int = when (this) {
    PlantCategory.WILDFLOWER -> R.string.category_wildflower
    PlantCategory.FOLIAGE -> R.string.category_foliage
    PlantCategory.BULB -> R.string.category_bulb
    PlantCategory.OTHER -> R.string.category_other
}

fun PlantCategory.barColor(): Color = when (this) {
    PlantCategory.WILDFLOWER -> AppColors.Terracotta
    PlantCategory.FOLIAGE -> AppColors.Leaf
    PlantCategory.BULB -> AppColors.CategoryBulb
    PlantCategory.OTHER -> AppColors.ChartBarDefault
}
