package com.devhjs.plantdex.data.datasource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.net.toUri
import com.devhjs.plantdex.domain.datasource.PhotoLoader
import com.devhjs.plantdex.domain.model.PlantPhoto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

private const val MAX_EDGE = 1568
private const val JPEG_QUALITY = 85

/**
 * file · content 양쪽을 읽어 장변 [MAX_EDGE] 의 JPEG 으로 다시 인코딩한다.
 * 원본 그대로 보내면 요청이 수 MB 가 되어 프로바이더 상한에 걸린다.
 */
class AndroidPhotoLoader @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : PhotoLoader {

    override suspend fun load(photoUri: String): PlantPhoto? = withContext(Dispatchers.IO) {
        runCatching {
            val uri = photoUri.toUri()
            val decoded = decode(uri) ?: return@runCatching null
            val fitted = decoded.rotated(orientationOf(uri)).fitToMaxEdge()

            ByteArrayOutputStream().use { out ->
                fitted.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out)
                PlantPhoto(out.toByteArray())
            }
        }.getOrNull()
    }

    /** 크기를 먼저 읽어 sample 을 정하고 두 번째에 실제로 디코딩한다. */
    private fun decode(uri: Uri): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        open(uri) { BitmapFactory.decodeStream(it, null, bounds) }

        val longEdge = maxOf(bounds.outWidth, bounds.outHeight)
        if (longEdge <= 0) return null

        val options = BitmapFactory.Options().apply {
            inSampleSize = sampleSizeFor(longEdge, MAX_EDGE)
        }
        return open(uri) { BitmapFactory.decodeStream(it, null, options) }
    }

    /** 갤러리 사진은 회전이 EXIF 에만 있어서 픽셀을 직접 돌려야 분석기가 바로 본다. */
    private fun orientationOf(uri: Uri): Int =
        open(uri) {
            ExifInterface(it).getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL,
            )
        } ?: ExifInterface.ORIENTATION_NORMAL

    private fun <T> open(uri: Uri, read: (InputStream) -> T): T? =
        context.contentResolver.openInputStream(uri)?.use(read)

    private fun Bitmap.rotated(orientation: Int): Bitmap {
        val degrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> return this
        }
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private fun Bitmap.fitToMaxEdge(): Bitmap {
        val longEdge = maxOf(width, height)
        if (longEdge <= MAX_EDGE) return this

        val scale = MAX_EDGE.toFloat() / longEdge
        return Bitmap.createScaledBitmap(
            this,
            (width * scale).toInt().coerceAtLeast(1),
            (height * scale).toInt().coerceAtLeast(1),
            true,
        )
    }
}
