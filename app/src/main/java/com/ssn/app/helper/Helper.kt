package com.ssn.app.helper

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.permissionx.guolindev.PermissionMediator
import com.ssn.app.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import zlc.season.downloadx.Progress
import zlc.season.downloadx.State
import zlc.season.downloadx.core.DownloadTask
import zlc.season.downloadx.download
import java.io.File

object Helper {
    fun isDebugBuild(): Boolean = BuildConfig.DEBUG
    fun isAndroidQ(): Boolean = Build.VERSION.SDK_INT == Build.VERSION_CODES.Q
    fun isAndroidQOrHigher(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    fun isAndroidROrHigher(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    fun showAlertDialog(
        context: Context,
        title: String,
        positiveButtonText: String,
        message: String? = null,
        negativeButtonText: String? = null,
        onPositiveButtonClicked: (dialog: DialogInterface) -> Unit = { dialog -> dialog.dismiss() },
        onNegativeButtonClicked: (dialog: DialogInterface) -> Unit = { dialog -> dialog.dismiss() }
    ) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setPositiveButton(positiveButtonText) { dialog, _ ->
                onPositiveButtonClicked.invoke(dialog)
            }
            if (!message.isNullOrEmpty()) {
                setMessage(message)
            }
            if (!negativeButtonText.isNullOrEmpty()) {
                setNegativeButton(negativeButtonText) { dialog, _ ->
                    onNegativeButtonClicked.invoke(dialog)
                }
            }
        }.show()
    }

    fun Context.showImagePickerSourceDialog(
        onCamera: () -> Unit,
        onFile: () -> Unit
    ) {
        val items = arrayOf("Kamera", "File")
        MaterialAlertDialogBuilder(this).apply {
            setItems(items) { dialog, index ->
                when (index) {
                    0 -> onCamera.invoke()
                    1 -> onFile.invoke()
                }
                dialog.dismiss()
            }
        }.show()
    }

    fun PermissionMediator.camera(action: () -> Unit) {
        this.permissions(Manifest.permission.CAMERA).onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(
                deniedList,
                "Core fundamental are based on these permissions",
                "OK",
                "Cancel"
            )
        }.onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                "You need to allow necessary permissions in Settings manually",
                "OK",
                "Cancel"
            )
        }.request { allGranted, _, _ ->
            if (allGranted) {
                action.invoke()
            }
        }
    }

    fun PermissionMediator.storage(action: () -> Unit) {
        val permission = mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!isAndroidQOrHigher()) {
            permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        this.permissions(permission).onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(
                deniedList,
                "Core fundamental are based on these permissions",
                "OK",
                "Cancel"
            )
        }.onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                "You need to allow necessary permissions in Settings manually",
                "OK",
                "Cancel"
            )
        }.request { allGranted, _, _ ->
            if (allGranted) {
                action.invoke()
            }
        }
    }

    fun CoroutineScope.handleDownload(
        url: String,
        onError: (Throwable) -> Unit,
        onFailed: () -> Unit,
        onSuccess: (File?) -> Unit,
        onDownloading: (Progress) -> Unit,
        onComplete: () -> Unit,
        onStopped: (() -> Unit)? = null,
        onWaiting: (() -> Unit)? = null
    ) {
        if (url.isEmpty()) return
        launch {
            val task = download(url)
            task.state()
                .onStart { task.start() }
                .onCompletion { task.stop(); onComplete.invoke() }
                .catch { e -> onError.invoke(e) }
                .distinctUntilChanged()
                .onEach { state ->
                    when (state) {
                        is State.Waiting -> onWaiting?.invoke()
                        is State.Downloading -> onDownloading.invoke(state.progress)
                        is State.Succeed -> {
                            onSuccess.invoke(task.file())
                            cancel()
                        }
                        is State.Failed -> {
                            onFailed.invoke()
                            cancel()
                        }
                        is State.Stopped -> {
                            onStopped?.invoke()
                            cancel()
                        }
                        is State.None -> Unit // initial state, do nothing
                    }
                }.launchIn(this)
        }
    }
}
