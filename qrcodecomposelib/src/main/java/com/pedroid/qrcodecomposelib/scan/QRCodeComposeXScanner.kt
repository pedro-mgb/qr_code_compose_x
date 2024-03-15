package com.pedroid.qrcodecomposelib.scan

import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.pedroid.qrcodecomposelib.scan.internal.ZxingAnalyzer
import com.pedroid.qrcodecomposelib.scan.internal.drawScannerFrame

@Composable
@RequiresPermission(android.Manifest.permission.CAMERA)
fun QRCodeComposeXScanner(
    modifier: Modifier = Modifier,
    frameColor: Color,
    onResult: (QRCodeScanResult) -> Unit,
    analyzer: QRCodeCameraAnalyzer = defaultQRCodeAnalyzer(onResult),
    frameVerticalPercent: Float = 0.5f,
    androidContext: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val cameraProviderFuture =
        remember {
            ProcessCameraProvider.getInstance(androidContext)
        }
    Box(
        modifier = modifier,
    ) {
        AndroidView(
            modifier =
                Modifier
                    .matchParentSize(),
            factory = { context ->
                val cameraPreviewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = buildBackCameraSelector()
                preview.setSurfaceProvider(cameraPreviewView.surfaceProvider)
                val imageAnalysis = buildImageAnalysis()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    analyzer,
                )
                try {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis,
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                cameraPreviewView
            },
        )
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .drawScannerFrame(frameColor, frameVerticalPercent),
        )
    }
}

fun defaultQRCodeAnalyzer(onResult: (QRCodeScanResult) -> Unit): QRCodeCameraAnalyzer = ZxingAnalyzer(onResult)

private fun buildBackCameraSelector() =
    CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

private fun buildImageAnalysis() =
    ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
