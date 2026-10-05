package com.kirikir.player.runtime

import android.content.Context
import com.kirikir.player.games.GameInfo
import java.io.File

interface RuntimeAdapter {
    val id: String
    fun isAvailable(): Boolean
    fun prepare(context: Context, game: GameInfo): Result<File>
    fun launch(context: Context, gameDir: File): Result<Unit>
}
