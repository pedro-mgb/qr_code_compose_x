package com.pedroid.qrcode_compose_x.generate.internal

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.pedroid.qrcode_compose_x.generate.QRCodeGenerateResult

/**
 * Returns the generated QR Code bitmap, or null if
 */
internal fun generateQRCodeViaZxing(
    text: String,
    size: Int,
    @ColorInt colorFill: Int = DEFAULT_FILL_COLOR
): QRCodeGenerateResult {
    return try {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix: BitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, size, size)
        createQRCodeBitmap(
            bitMatrix,
            size,
            colorFill
        ).let {
            QRCodeGenerateResult.Generated(it)
        }
    } catch (wEx: WriterException) {
        wEx.printStackTrace()
        QRCodeGenerateResult.Error
    }
}

private fun createQRCodeBitmap(
    matrix: BitMatrix,
    size: Int,
    @ColorInt colorFill: Int
): Bitmap {
    val pixels: IntArray = List(matrix.width * matrix.height) {
        if (matrix.get(it % matrix.width, it / matrix.height)) colorFill else DEFAULT_BLANK_COLOR
    }.toIntArray()
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, size, 0, 0, matrix.width, matrix.height)
    return bitmap
}