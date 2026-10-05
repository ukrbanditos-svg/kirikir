package com.kirikir.player.runtime

object NativeKirikiriBridge {
    private var loaded = false

    init {
        loaded = try {
            System.loadLibrary("kirikir_runtime")
            true
        } catch (_: UnsatisfiedLinkError) {
            false
        }
    }

    fun isAvailable(): Boolean = loaded

    external fun nativeVersion(): String
    external fun nativeProbeGame(path: String): Int
}
