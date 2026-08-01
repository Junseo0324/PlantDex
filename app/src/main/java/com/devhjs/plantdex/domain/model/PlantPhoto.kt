package com.devhjs.plantdex.domain.model

enum class PhotoFormat(val mimeType: String) {
    JPEG("image/jpeg"),
    PNG("image/png"),
}

/**
 * 분석에 넘길 사진. Bitmap/Uri 같은 Android 타입을 담지 않으므로
 *
 * data class 가 아닌 이유: ByteArray 를 넣으면 equals 는 참조 비교인데
 * hashCode·toString 은 내용 기반이 되어 동작이 엇갈린다. 내용 동일성을 물을 일도 없다.
 */
class PlantPhoto(
    val bytes: ByteArray,
    val format: PhotoFormat = PhotoFormat.JPEG,
)
