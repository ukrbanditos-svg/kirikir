package com.kirikir.player.runtime

data class RuntimeStatus(
    val nativeLoaded: Boolean,
    val version: String?
) {
    companion object {
        fun current(): RuntimeStatus {
            val ok = NativeKirikiriBridge.isAvailable()
            return RuntimeStatus(ok, if (ok) NativeKirikiriBridge.nativeVersion() else null)
        }
    }
}
