package com.ssn.app.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel : ViewModel() {
    protected val _messageChannel: Channel<String> = Channel()
    val messageChannel = _messageChannel.receiveAsFlow()
}
