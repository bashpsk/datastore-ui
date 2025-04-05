package io.bash_psk.datastore_ui.utils

import android.util.Log

internal object SetLog {

    private const val DEFAULT_TAG = "PSK"

    fun debug(tag: String = DEFAULT_TAG, message: String) {

        Log.d(tag, message)
    }

    fun debug(tag: String = DEFAULT_TAG, throwable: Throwable?) {

        Log.d(tag, findLogMessage(throwable = throwable), throwable)
    }

    fun error(tag: String = DEFAULT_TAG, message: String) {

        Log.e(tag, message)
    }

    fun error(tag: String = DEFAULT_TAG, throwable: Throwable?) {

        Log.e(tag, findLogMessage(throwable = throwable), throwable)
    }

    fun info(tag: String = DEFAULT_TAG, message: String) {

        Log.i(tag, message)
    }

    fun info(tag: String = DEFAULT_TAG, throwable: Throwable?) {

        Log.i(tag, findLogMessage(throwable = throwable), throwable)
    }

    fun verbose(tag: String = DEFAULT_TAG, message: String) {

        Log.v(tag, message)
    }

    fun verbose(tag: String = DEFAULT_TAG, throwable: Throwable?) {

        Log.v(tag, findLogMessage(throwable = throwable), throwable)
    }

    fun warning(tag: String = DEFAULT_TAG, message: String) {

        Log.w(tag, message)
    }

    fun warning(tag: String = DEFAULT_TAG, throwable: Throwable?) {

        Log.w(tag, findLogMessage(throwable = throwable), throwable)
    }

    fun findLogMessage(throwable: Throwable?): String {

        val stackTrace = throwable?.stackTrace?.firstOrNull()
        val fileName = stackTrace?.fileName ?: "Unknown"
        val line = stackTrace?.lineNumber ?: "Unknown"
        val message = throwable?.message ?: "Unknown"

        return "File - $fileName, Line - $line, Message - $message"
    }
}