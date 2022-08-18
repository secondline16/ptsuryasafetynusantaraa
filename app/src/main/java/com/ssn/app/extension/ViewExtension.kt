package com.ssn.app.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.viewbinding.ViewBinding
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.ssn.app.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

inline fun <T : ViewBinding> ViewGroup.inflateBinding(
    crossinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
    attachToParent: Boolean = false
) = bindingInflater.invoke(LayoutInflater.from(this.context), this, attachToParent)

fun View.showSnackBar(@StringRes message: Int) {
    showSnackBar(context.getString(message))
}

fun View.showSnackBar(message: String) {
    if (message.isEmpty()) return
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.delayOnLifecycle(
    durationInMillis: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(durationInMillis)
        block.invoke()
    }
}

fun ImageView.setDocumentIcon() {
    load(R.drawable.ic_document)
}
