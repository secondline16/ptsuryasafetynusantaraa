package com.ssn.app.helper

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.ssn.app.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FilePickerHelper {

    const val EXTENSION_PNG = ".png"
    const val EXTENSION_JPG = ".jpg"
    const val EXTENSION_JPEG = ".jpeg"
    private const val APPLICATION_TYPE = "application/*"
    private const val ALL_IMAGE_TYPE = "image/*"
    private const val MS_WORD_DOC_TYPE = "application/msword"
    private const val MS_WORD_DOCX_TYPE =
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    private const val PDF_TYPE = "application/pdf"

    fun getSingleFilePickIntent(): Intent {
        val typesAllowed = arrayOf(
            MS_WORD_DOC_TYPE,
            MS_WORD_DOCX_TYPE,
            PDF_TYPE,
            ALL_IMAGE_TYPE
        )
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_MIME_TYPES, typesAllowed)
            putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            type = APPLICATION_TYPE
        }
    }

    fun getSingleImagePickIntent(): Intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        type = ALL_IMAGE_TYPE
    }

    private fun generateTempName(): String = "temp_${System.currentTimeMillis()}"

    fun Context.getTempFileUri(
        name: String = generateTempName(),
        format: String = EXTENSION_PNG
    ): Uri {
        val tmpFile = File.createTempFile(name, format, cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return tmpFile.getUri(applicationContext)
    }

    fun File.getUri(context: Context): Uri = FileProvider.getUriForFile(
        context.applicationContext,
        "${BuildConfig.APPLICATION_ID}.provider",
        this
    )

    fun Uri?.doIfImage(context: Context, action: (File) -> Unit, onNotImage: () -> Unit) {
        this?.let { uri ->
            val file = getFilePathFromUri(context, uri).run(::File)
            val isImage = file.name.run {
                contains(EXTENSION_PNG) || contains(EXTENSION_JPG) || contains(EXTENSION_JPEG)
            }
            if (isImage) action.invoke(file) else onNotImage.invoke()
        }
    }

    fun getFilePathFromUri(context: Context, uri: Uri): String =
        if (uri.path?.contains("file://") == true) uri.path.orEmpty()
        else getFileFromContentUri(context, uri).path.orEmpty()

    private fun getFileFromContentUri(
        context: Context,
        contentUri: Uri,
        uniqueName: Boolean = true
    ): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri) ?: ""
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = ("temp_file_" + if (uniqueName) timeStamp else "") + ".$fileExtension"
        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()
        // Initialize streams
        var oStream: FileOutputStream? = null
        var inputStream: InputStream? = null

        try {
            oStream = FileOutputStream(tempFile)
            inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let { copy(inputStream, oStream) }
            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Close streams
            inputStream?.close()
            oStream?.close()
        }

        return tempFile
    }

    private fun getFileExtension(
        context: Context,
        uri: Uri
    ): String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(context.contentResolver.getType(uri))
    } else uri.path?.let {
        MimeTypeMap.getFileExtensionFromUrl(
            Uri.fromFile(File(it)).toString()
        )
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }
}
