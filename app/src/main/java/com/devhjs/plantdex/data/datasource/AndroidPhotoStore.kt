package com.devhjs.plantdex.data.datasource

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.devhjs.plantdex.domain.datasource.PhotoStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Clock

private const val PHOTO_DIR = "photos"

/**
 * filesDir 에 보관한다. 캐시 정리로는 지워지지 않고, 앱 데이터를 지울 때 도감 DB 와 함께 사라진다.
 */
class AndroidPhotoStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val clock: Clock,
) : PhotoStore {

    private val photosDir: File
        get() = File(context.filesDir, PHOTO_DIR).apply { mkdirs() }

    override suspend fun save(sourceUri: String): String? = withContext(Dispatchers.IO) {
        runCatching {
            val target = File(photosDir, newFileName())
            context.contentResolver.openInputStream(sourceUri.toUri())?.use { input ->
                target.outputStream().use(input::copyTo)
            } ?: return@runCatching null

            Uri.fromFile(target).toString()
        }.getOrNull()
    }

    override suspend fun delete(photoUri: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                val file = photoUri.toUri().toFileOrNull() ?: return@runCatching
                if (file.parentFile == photosDir) file.delete()
            }
        }
    }

    /** 갤러리에서 여러 장을 잇달아 복사해도 겹치지 않게 uuid 를 붙인다. */
    private fun newFileName(): String =
        "${clock.now().toEpochMilliseconds()}-${UUID.randomUUID()}.jpg"

    private fun Uri.toFileOrNull(): File? =
        path?.takeIf { scheme == "file" }?.let(::File)
}
