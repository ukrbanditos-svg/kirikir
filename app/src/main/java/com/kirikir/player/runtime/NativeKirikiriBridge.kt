package com.kirikir.player.runtime

object NativeKirikiriBridge {
    private var loaded = false
    private var lastLoadError: String? = null

    init {
        loaded = try {
            System.loadLibrary("kirikir_runtime")
            true
        } catch (t: Throwable) {
            lastLoadError = t.toString()
            false
        }
    }

    fun isAvailable(): Boolean = loaded
    fun loadError(): String? = lastLoadError

    external fun nativeVersion(): String
    external fun nativeProbeGame(path: String): Int
}
