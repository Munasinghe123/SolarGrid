package com.example.solargrid.common

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

object QrCodeGenerator {

    /**
     * Generates a square QR Code Bitmap from the given token/data string.
     *
     * @param data The text/JWT token to encode.
     * @param size Width and height of the generated QR image in pixels (e.g. 512).
     * @return Bitmap of the generated QR code, or null if an error occurs.
     */
    fun generateQrCode(data: String, size: Int = 512): Bitmap? {
        if (data.isBlank()) return null

        return try {
            val bitMatrix = MultiFormatWriter().encode(
                data,
                BarcodeFormat.QR_CODE,
                size,
                size
            )

            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                }
            }

            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
                setPixels(pixels, 0, width, 0, 0, width, height)
            }
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
