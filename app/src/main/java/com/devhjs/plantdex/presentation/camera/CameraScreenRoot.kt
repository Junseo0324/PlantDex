package com.devhjs.plantdex.presentation.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.component.AppIconButton
import com.devhjs.plantdex.presentation.component.PermissionNotice
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppSpacing

@Composable
fun CameraScreenRoot(
    onCaptured: (photoUri: String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current

    var isGranted by remember { mutableStateOf(context.isCameraGranted()) }
    var hasAsked by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        isGranted = granted
        hasAsked = true
    }

    // 취소하면 null 이 온다. 그때는 아무 일도 일어나지 않아야 한다.
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        uri?.let { viewModel.onAction(CameraAction.PickedFromGallery(it.toString())) }
    }

    LaunchedEffect(Unit) {
        if (!isGranted) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // 설정에서 켜고 돌아오는 경로가 있어 복귀할 때마다 다시 읽는다.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) isGranted = context.isCameraGranted()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (isGranted) {
        val controller = rememberCameraXController(state)
        val captureFailed = stringResource(R.string.camera_capture_failed)

        LaunchedEffect(viewModel, controller) {
            viewModel.event.collect { event ->
                when (event) {
                    CameraEvent.RequestCapture ->
                        viewModel.onAction(CameraAction.Captured(controller.capture()))
                    is CameraEvent.Captured -> onCaptured(event.photoUri)
                    CameraEvent.CaptureFailed ->
                        Toast.makeText(context, captureFailed, Toast.LENGTH_SHORT).show()
                    CameraEvent.RequestGallery -> galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                    is CameraEvent.RequestFocus -> controller.focusAt(event.offset)
                    CameraEvent.Close -> onClose()
                }
            }
        }

        CameraScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = modifier,
            previewContent = {
                AndroidView(
                    factory = { controller.previewView },
                    modifier = Modifier.fillMaxSize(),
                )
            },
        )
    } else {
        CameraPermissionGate(
            isPermanentlyDenied = hasAsked && activity?.canAskCameraAgain() == false,
            onRequest = { permissionLauncher.launch(Manifest.permission.CAMERA) },
            onOpenSettings = { context.startActivity(appSettingsIntent(context.packageName)) },
            onClose = onClose,
            modifier = modifier,
        )
    }
}

/** 권한이 없을 때도 화면을 빠져나갈 수 있어야 해서 닫기를 함께 둔다. */
@Composable
private fun CameraPermissionGate(
    isPermanentlyDenied: Boolean,
    onRequest: () -> Unit,
    onOpenSettings: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Charcoal),
    ) {
        Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.screenH, vertical = 8.dp),
            ) {
                AppIconButton(
                    iconRes = R.drawable.ic_close,
                    contentDescription = stringResource(R.string.camera_close),
                    onClick = onClose,
                    containerColor = AppColors.OnDark.copy(alpha = 0.14f),
                    tint = AppColors.OnDark,
                )
            }

            PermissionNotice(
                title = stringResource(
                    if (isPermanentlyDenied) {
                        R.string.camera_permission_denied_title
                    } else {
                        R.string.camera_permission_title
                    },
                ),
                description = stringResource(
                    if (isPermanentlyDenied) {
                        R.string.camera_permission_denied_body
                    } else {
                        R.string.camera_permission_body
                    },
                ),
                actionLabel = stringResource(
                    if (isPermanentlyDenied) {
                        R.string.camera_permission_denied_action
                    } else {
                        R.string.camera_permission_action
                    },
                ),
                onAction = if (isPermanentlyDenied) onOpenSettings else onRequest,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private fun android.content.Context.isCameraGranted(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
        PackageManager.PERMISSION_GRANTED

/** 거부한 뒤에도 rationale 을 보여줄 수 있으면 다시 물을 수 있다. */
private fun Activity.canAskCameraAgain(): Boolean =
    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)

private fun appSettingsIntent(packageName: String): Intent =
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null),
    )
