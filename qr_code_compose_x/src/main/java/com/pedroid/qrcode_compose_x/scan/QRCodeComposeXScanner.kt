package com.pedroid.qrcode_compose_x.scan

import android.content.Context
import android.util.Size
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.pedroid.qrcode_compose_x.scan.internal.QrCodeAnalyzer

@Composable
@RequiresPermission(android.Manifest.permission.CAMERA)
fun QRCodeComposeXScanner(
    modifier: Modifier = Modifier,
    onResult: (QRCodeScanResult) -> Unit,
    androidContext: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(androidContext)
    }
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val cameraPreviewView = PreviewView(context)
            val preview = Preview.Builder().build()
            val selector = buildBackCameraSelector()
            preview.setSurfaceProvider(cameraPreviewView.surfaceProvider)
            val imageAnalysis = buildImageAnalysis(cameraPreviewView)
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context),
                QrCodeAnalyzer(onResult)
            )
            try {
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            cameraPreviewView
        }
    )
}

private fun buildBackCameraSelector() = CameraSelector.Builder()
    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
    .build()

private fun buildImageAnalysis(previewView: View) = ImageAnalysis.Builder()
    .setTargetResolution(
        Size(
            previewView.width,
            previewView.height
        )
    )
    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
    .build()