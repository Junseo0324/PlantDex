package com.devhjs.plantdex.presentation.text

import androidx.annotation.StringRes
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.camera.CameraFlash

@StringRes
fun CameraFlash.labelRes(): Int = when (this) {
    CameraFlash.AUTO -> R.string.camera_flash_auto
    CameraFlash.ON -> R.string.camera_flash_on
    CameraFlash.OFF -> R.string.camera_flash_off
}
