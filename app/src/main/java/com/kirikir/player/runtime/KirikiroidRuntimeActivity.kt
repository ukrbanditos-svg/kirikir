package com.kirikir.player.runtime

import android.os.Bundle
import org.tvp.kirikiri2.KR2Activity

class KirikiroidRuntimeActivity : KR2Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        RuntimeTrace.mark(this, "runtime-activity:before-super")
        super.onCreate(savedInstanceState)
        RuntimeTrace.mark(this, "runtime-activity:after-super")
    }

    override fun getStoragePath(): Array<String> {
        val gamePath = intent?.getStringExtra(EXTRA_GAME_PATH)
        return if (!gamePath.isNullOrBlank()) arrayOf(gamePath) else super.getStoragePath()
    }

    override fun get_res_sd_operate_step(): Int = -1

    companion object {
        const val EXTRA_GAME_PATH = "kirikir.game.path"
    }
}
