package com.kirikir.player.runtime

import android.content.Context
import java.io.File

object RuntimeTrace {
    private const val FILE_NAME = "runtime_startup_state.txt"

    @JvmStatic
    fun mark(context: Context?, stage: String) {
        if (context == null) return
        try {
            File(context.filesDir, FILE_NAME).writeText(stage)
        } catch (_: Throwable) {}
    }

    @JvmStatic
    fun read(context: Context): String? = try {
        val f = File(context.filesDir, FILE_NAME)
        if (f.exists()) f.readText().trim().ifEmpty { null } else null
    } catch (_: Throwable) {
        null
    }

    @JvmStatic
    fun clear(context: Context) {
        try { File(context.filesDir, FILE_NAME).delete() } catch (_: Throwable) {}
    }
}
