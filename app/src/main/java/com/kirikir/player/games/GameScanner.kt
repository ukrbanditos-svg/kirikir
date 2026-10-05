package com.kirikir.player.games
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
class GameScanner(private val context:Context){fun scan(rootUri:Uri):GameInfo?{val root=DocumentFile.fromTreeUri(context,rootUri)?:return null;val xp3=root.listFiles().filter{it.isFile&&it.name?.endsWith(".xp3",true)==true}.mapNotNull{it.name}.sorted();if(xp3.isEmpty())return null;return GameInfo(root.name?:"KiriKiri game",rootUri.toString(),xp3)}}