package com.mlx.administrator.myapplication.nfc

import kotlin.experimental.and

class ByteArrayChange {

    companion object{
        fun ByteArrayToHexString(bytesId: ByteArray):String{
            var i=0
            var input=0
            val hex = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
            var output=""
            bytesId.forEachIndexed { index, byte ->
                input = (bytesId[index] and 0xff.toByte()).toInt()
                i = input shr 4 and 0x0f
                output += hex[i]
                i = input and 0x0f
                output += hex[i]
            }
            return output
        }
    }
}