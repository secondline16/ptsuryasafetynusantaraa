package com.ssn.app.helper

import android.content.Context
import android.content.Intent
import com.ssn.app.helper.FilePickerHelper.getUri
import java.io.File

object FileHelper {

    fun isPlainTextFile(fileName: String): Boolean = fileName.contains(".txt", true)

    fun isDocumentFile(fileName: String): Boolean =
        fileName.contains(".doc", true) or fileName.contains(".docx", true)

    fun isExcelFile(fileName: String): Boolean =
        fileName.contains(".xls", true) or fileName.contains(".xlsx", true)

    fun isImageFile(fileName: String): Boolean =
        fileName.contains(".png", true) or fileName.contains(
            ".jpg",
            true
        ) or fileName.contains(".jpeg", true)

    fun isPdfFile(fileName: String): Boolean =
        fileName.contains(".pdf", true)

    fun isPowerPointFile(fileName: String): Boolean =
        fileName.contains(".ppt", true) or fileName.contains(".pptx", true)

    fun isZipFile(fileName: String): Boolean =
        fileName.contains(".zip", true)

    fun fileExt(data: String): String = when {
        isDocumentFile(data) -> "application/msword"
        isExcelFile(data) -> "application/vnd.ms-excel"
        isImageFile(data) -> "image/*"
        isPdfFile(data) -> "application/pdf"
        isPowerPointFile(data) -> "application/vnd.ms-powerpoint"
        isZipFile(data) -> "application/zip"
        else -> "*/*"
    }

    fun File.open(context: Context) {
        Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(
                getUri(context),
                absolutePath?.let { fileExt(it) }
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }.also { intent ->
            val target = Intent.createChooser(intent, "Open file")
            target.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(target)
        }
    }
}
