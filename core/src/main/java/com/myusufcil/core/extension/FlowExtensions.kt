package com.myusufcil.core.extension

import com.myusufcil.core.networking.DataFetchResult
import kotlinx.coroutines.flow.FlowCollector


suspend fun <T> FlowCollector<DataFetchResult<T>>.success(t: T) {
    with(receiver = this) {
        loading(isLoading = false)
        emit(DataFetchResult.success(t))
    }
}

suspend fun <T> FlowCollector<DataFetchResult<T>>.loading(isLoading: Boolean) {
    emit(DataFetchResult.loading(isLoading))
}

suspend fun <T> FlowCollector<DataFetchResult<T>>.failed(e: Throwable) {
    with(receiver = this) {
        loading(isLoading = false)
        emit(DataFetchResult.failure(e))
    }
}