package com.mlx.administrator.myapplication.openGL.lesson1

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.ArrayList

class FloatBufferUtil {
    companion object {

        // 以下定义了立方体六个面的顶点坐标数组（每个坐标点都由三个浮点数组成）
        private val verticesFront =
            floatArrayOf(1f, 1f, 1f, 1f, 1f, -1f, -1f, 1f, -1f, -1f, 1f, 1f)
        private val verticesBack =
            floatArrayOf(1f, -1f, 1f, 1f, -1f, -1f, -1f, -1f, -1f, -1f, -1f, 1f)
        private val verticesTop =
            floatArrayOf(1f, 1f, 1f, 1f, -1f, 1f, -1f, -1f, 1f, -1f, 1f, 1f)
        private val verticesBottom =
            floatArrayOf(1f, 1f, -1f, 1f, -1f, -1f, -1f, -1f, -1f, -1f, 1f, -1f)
        private val verticesLeft =
            floatArrayOf(-1f, 1f, 1f, -1f, 1f, -1f, -1f, -1f, -1f, -1f, -1f, 1f)
        private val verticesRight =
            floatArrayOf(1f, 1f, 1f, 1f, 1f, -1f, 1f, -1f, -1f, 1f, -1f, 1f)

        fun getFloatBuffer(array: FloatArray): FloatBuffer {
            // 初始化字节缓冲区的大小=数组长度*数组元素大小。float类型的元素大小为Float.SIZE，
            // int类型的元素大小为Integer.SIZE，double类型的元素大小为Double.SIZE。
            val byteBuffer = ByteBuffer.allocateDirect(array.size * java.lang.Float.SIZE)
            // 以本机字节顺序来修改字节缓冲区的字节顺序
            // OpenGL在底层的实现是C语言，与Java默认的数据存储字节顺序可能不同，即大端小端问题。
            // 因此，为了保险起见，在将数据传递给OpenGL之前，需要指明使用本机的存储顺序
            byteBuffer.order(ByteOrder.nativeOrder())
            // 根据设置好的参数构造浮点缓冲区
            val floatBuffer = byteBuffer.asFloatBuffer()
            // 把数组数据写入缓冲区
            floatBuffer.put(array)
            // 设置浮点缓冲区的初始位置
            floatBuffer.position(0)
            return floatBuffer
        }

        // 获得立方体的顶点队列
        fun getCubeVertexs(): ArrayList<FloatBuffer> {
            val verticeArray = ArrayList<FloatBuffer>()
            verticeArray.add(getFloatBuffer(verticesFront))
            verticeArray.add(getFloatBuffer(verticesBack))
            verticeArray.add(getFloatBuffer(verticesTop))
            verticeArray.add(getFloatBuffer(verticesBottom))
            verticeArray.add(getFloatBuffer(verticesLeft))
            verticeArray.add(getFloatBuffer(verticesRight))
            return verticeArray
        }


        // 获得球体的顶点队列
        fun getBallVertices(divide: Int, radius: Int): ArrayList<FloatBuffer> {
            val verticeArray = ArrayList<FloatBuffer>()
            var latitude: Float // 纬度
            var latitudeNext: Float // 下一层纬度
            var longitude: Float // 经度
            var pointX: Float // 点坐标x
            var pointY: Float // 点坐标y
            var pointZ: Float // 点坐标z
            // 将纬度等分成divide份，这样就能计算出每一等份的纬度值
            for (i in 0..divide) {
                // 获取当前等份的纬度值
                latitude = (Math.PI / 2.0 - i * Math.PI / divide).toFloat()
                // 获取下一等份的纬度值
                latitudeNext = (Math.PI / 2.0 - (i + 1) * Math.PI / divide).toFloat()
                // 当前纬度和下一纬度的点坐标
                val vertices = FloatArray(divide * 6 + 6)
                // 将经度等分成divide份，这样就能得到当前纬度值和下一纬度值的每一份经度值
                for (j in 0..divide) {
                    // 计算经度值
                    longitude = (j * (Math.PI * 2) / divide).toFloat()
                    pointX =
                        (Math.cos(latitude.toDouble()) * Math.cos(longitude.toDouble())).toFloat()
                    pointY = Math.sin(latitude.toDouble()).toFloat()
                    pointZ =
                        (-(Math.cos(latitude.toDouble()) * Math.sin(longitude.toDouble()))).toFloat()
                    // 此经度值下的当前纬度的点坐标
                    vertices[6 * j + 0] = radius * pointX
                    vertices[6 * j + 1] = radius * pointY
                    vertices[6 * j + 2] = radius * pointZ
                    pointX =
                        (Math.cos(latitudeNext.toDouble()) * Math.cos(longitude.toDouble())).toFloat()
                    pointY = Math.sin(latitudeNext.toDouble()).toFloat()
                    pointZ =
                        (-(Math.cos(latitudeNext.toDouble()) * Math.sin(longitude.toDouble()))).toFloat()
                    // 此经度值下的下一纬度的点坐标
                    vertices[6 * j + 3] = radius * pointX
                    vertices[6 * j + 4] = radius * pointY
                    vertices[6 * j + 5] = radius * pointZ
                }
                // 将点坐标转换成FloatBuffer类型添加到点坐标集合ArrayList<FloatBuffer>里
                verticeArray.add(getFloatBuffer(vertices))
            }
            return verticeArray
        }

        // 获得球体的纹理坐标
        fun getTextureCoords(divide: Int): ArrayList<FloatBuffer> {
            val textureArray = ArrayList<FloatBuffer>()
            for (i in 0..divide) {
                val texCoords = FloatArray(divide * 4 + 4)
                for (j in 0..divide) {
                    texCoords[4 * j + 0] = j / divide.toFloat()
                    texCoords[4 * j + 1] = i / divide.toFloat()
                    texCoords[4 * j + 2] = j / divide.toFloat()
                    texCoords[4 * j + 3] = (i + 1) / divide.toFloat()
                }
                textureArray.add(getFloatBuffer(texCoords))
            }
            return textureArray
        }
    }
}