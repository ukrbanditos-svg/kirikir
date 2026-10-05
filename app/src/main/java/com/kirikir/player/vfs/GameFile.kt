package com.kirikir.player.vfs
interface GameFile:AutoCloseable{val size:Long;fun read(offset:Long,length:Int):ByteArray}