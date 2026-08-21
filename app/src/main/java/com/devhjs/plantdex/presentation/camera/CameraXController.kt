package com.devhjs.plantdex.presentation.camera

import android.content.Context
import android.net.Uri
import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

/** 저장 해상도 상한. 원본 12MP 는 분석에 필요 이상으로 크다. */
private val CAPTURE_SIZE = Size(1080, 1920)

/**
 * CameraX 를 Compose 수명에 묶는다. [CameraState] 를 받아 하드웨어에 반영하고
 * 촬영 결과는 캐시의 임시 파일 위치로 돌려준다. 보관은 호출부가 UseCase 로 한다.
 */
class CameraXController(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
) {

    val previewView = PreviewView(context).apply {
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }

    private val preview = Preview.Builder().build()

    private val imageCapture = ImageCapture.Builder()
        .setResolutionSelector(
            ResolutionSelector.Builder()
                .setResolutionStrategy(
                    ResolutionStrategy(
                        CAPTURE_SIZE,
                        ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER_THEN_HIGHER,
                    ),
                )
                .build(),
        )
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    private val executor = ContextCompat.getMainExecutor(context)

    private var camera: Camera? = null
    private var lens = CameraLens.BACK
    private var zoomRatio = CameraZoom.X1.ratio

    suspend fun bind(lens: CameraLens) {
        this.lens = lens
        val provider = ProcessCameraProvider.awaitInstance(context)

        preview.setSurfaceProvider(previewView.surfaceProvider)
        provider.unbindAll()
        camera = provider.bindToLifecycle(lifecycleOwner, lens.selector(), preview, imageCapture)

        // 재바인딩하면 배율이 초기화되므로 마지막 선택을 다시 적용한다.
        setZoom(zoomRatio)
    }

    suspend fun unbind() {
        ProcessCameraProvider.awaitInstance(context).unbindAll()
        camera = null
    }

    fun setFlash(flash: CameraFlash) {
        imageCapture.flashMode = flash.mode()
    }

    fun setZoom(ratio: Float) {
        zoomRatio = ratio
        camera?.cameraControl?.setZoomRatio(ratio)
    }

    /** 기기를 눕히고 찍어도 바로 선 사진이 저장되게 한다. */
    fun setRotation(rotation: Int) {
        imageCapture.targetRotation = rotation
    }

    fun focusAt(offset: Offset) {
        val point = previewView.meteringPointFactory.createPoint(offset.x, offset.y)
        camera?.cameraControl?.startFocusAndMetering(FocusMeteringAction.Builder(point).build())
    }

    /** 캐시에 찍은 임시 파일의 위치. 실패하면 null. */
    suspend fun capture(): String? = suspendCancellableCoroutine { continuation ->
        val target = File(context.cacheDir, "capture-${System.currentTimeMillis()}.jpg")
        val options = ImageCapture.OutputFileOptions.Builder(target)
            .setMetadata(
                ImageCapture.Metadata().apply {
                    isReversedHorizontal = lens == CameraLens.FRONT
                },
            )
            .build()

        imageCapture.takePicture(
            options,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(results: ImageCapture.OutputFileResults) {
                    continuation.resume(
                        results.savedUri?.toString() ?: Uri.fromFile(target).toString(),
                    )
                }

                override fun onError(exception: ImageCaptureException) {
                    target.delete()
                    continuation.resume(null)
                }
            },
        )
    }

    private fun CameraLens.selector(): CameraSelector = when (this) {
        CameraLens.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
        CameraLens.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
    }

    private fun CameraFlash.mode(): Int = when (this) {
        CameraFlash.AUTO -> ImageCapture.FLASH_MODE_AUTO
        CameraFlash.ON -> ImageCapture.FLASH_MODE_ON
        CameraFlash.OFF -> ImageCapture.FLASH_MODE_OFF
    }
}

@Composable
fun rememberCameraXController(state: CameraState): CameraXController {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val controller = remember(lifecycleOwner) { CameraXController(context, lifecycleOwner) }

    LaunchedEffect(controller, state.lens) { controller.bind(state.lens) }
    LaunchedEffect(controller, state.flash) { controller.setFlash(state.flash) }
    LaunchedEffect(controller, state.zoom) { controller.setZoom(state.zoom.ratio) }

    DisposableEffect(controller) {
        val listener = object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return
                controller.setRotation(orientation.toSurfaceRotation())
            }
        }
        listener.enable()
        onDispose { listener.disable() }
    }

    return controller
}

/** 화면이 세로로 고정돼 있어 기기 각도를 직접 읽어야 한다. */
private fun Int.toSurfaceRotation(): Int = when {
    this >= 315 || this < 45 -> Surface.ROTATION_0
    this < 135 -> Surface.ROTATION_270
    this < 225 -> Surface.ROTATION_180
    else -> Surface.ROTATION_90
}
