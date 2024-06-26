package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScannedCode

data class ScanQRCodeInfoNavigationListeners(
    val onGoScanQRCodeWithCamera: () -> Unit,
    val onGoScanQRCodeFromFile: () -> Unit,
)

data class ScanQRCodeCameraNavigationListeners(
    val onCodeScanned: (ScannedCode) -> Unit,
    val onBackInvoked: () -> Unit,
)

data class StartScanActionListeners(
    val onStartScanFromCamera: () -> Unit = {},
    val onStartScanFromImageFile: () -> Unit = {},
)

data class ScannedQRCodeActionListeners(
    val onCodeOpen: (String) -> Unit = {},
    val onCodeShared: (String) -> Unit = {},
    val onCodeCopied: (String) -> Unit = {},
)
