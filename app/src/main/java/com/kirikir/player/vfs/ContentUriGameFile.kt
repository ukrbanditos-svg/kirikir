package com.kirikir.player.vfs
import android.content.Context
import android.net.Uri
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.ByteBuffer
class ContentUriGameFile(context:Context,uri:Uri):GameFile{private val descriptor=context.contentResolver.openFileDescriptor(uri,"r")?:throw FileNotFoundException(uri.toString());private val channel=FileInputStream(descriptor.fileDescriptor).channel;override val size:Long get()=channel.size();@Synchronized override fun read(offset:Long,length:Int):ByteArray{require(offset>=0);require(length>=0);if(offset>=size||length==0)return ByteArray(0);val wanted=minOf(length.toLong(),size-offset).toInt();val b=ByteBuffer.allocate(wanted);channel.position(offset);while(b.hasRemaining())if(channel.read(b)<0)break;return b.array().copyOf(b.position())};override fun close(){channel.close();descriptor.close()}}