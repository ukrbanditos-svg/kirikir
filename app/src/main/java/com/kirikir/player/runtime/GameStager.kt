package com.kirikir.player.runtime

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.kirikir.player.games.GameInfo
import java.io.File

class GameStager(private val context: Context) {
    fun stage(game: GameInfo, onProgress: (String) -> Unit = {}): Result<File> = runCatching {
        val root = DocumentFile.fromTreeUri(context, Uri.parse(game.rootUri)) ?: error("Не удалось открыть папку игры")
        val safeName = game.name.replace(Regex("[^A-Za-z0-9._-]"), "_")
        val out = File(context.filesDir, "runtime/$safeName")
        if (out.exists()) out.deleteRecursively()
        out.mkdirs()
        root.listFiles().filter { it.isFile }.forEach { src ->
            val name = src.name ?: return@forEach
            onProgress(name)
            val dst = File(out, name)
            context.contentResolver.openInputStream(src.uri).use { input ->
                requireNotNull(input) { "Не удалось прочитать $name" }
                dst.outputStream().use { output -> input.copyTo(output) }
            }
        }
        out
    }
}
