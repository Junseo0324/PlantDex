package com.devhjs.plantdex.domain.model

enum class PhotoFormat(val mimeType: String) {
    JPEG("image/jpeg"),
    PNG("image/png"),
}

/**
 * 분석에 넘길 사진. Bitmap/Uri 같은 Android 타입을 담지 않으므로
 *
 */
class PlantPhoto(
    val bytes: ByteArray,
    val format: PhotoFormat = PhotoFormat.JPEG,
)
