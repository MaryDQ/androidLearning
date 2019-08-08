package com.mlx.administrator.myapplication.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout


class MatrixPicView @kotlin.jvm.JvmOverloads constructor(context: Context, attr: AttributeSet? = null) :
    FrameLayout(context, attr) {
    private var type = 0
    private var paint: Paint = Paint()
    private var mMatrix = Matrix()
    private var bitmap =
        BitmapFactory.decodeResource(context.resources, com.mlx.administrator.myapplication.R.mipmap.ic_launcher)

    init {
//        setBackgroundResource(R.mipmap.ic_launcher)
//        invalidate()
        setBackgroundColor(Color.BLACK)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (null != canvas) {
//            drawWithType(canvas)
//            canvas.setMatrix(mMatrix)
//            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }

    }

    fun setMartixFromOut(matrix: Matrix) {
        mMatrix = matrix

        invalidate()
    }


    fun setMatrixValues(array: FloatArray) {
        this.translationX = array[5]
//        this.matrix.setValues(array)
        invalidate()
    }

    fun setAnimType(type: Int) {
        this.type = type
        invalidate()
    }


    /**
     * 根据当前的类型，进行图片的操作
     * 1.平移操作
     * 2.缩放操作
     * 3.旋转操作
     * 4.错切操作
     */
    private fun drawWithType(canvas: Canvas) {
        when (type) {
            0 -> {
                val bitmap2 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
                val canvas2 = Canvas(bitmap2)
                canvas2.drawColor(Color.WHITE)
                val rectPaint2 = Paint()
                rectPaint2.color = Color.GREEN
                canvas2.drawRect(20f, 20f, 180f, 180f, rectPaint2)
                val matrix2 = Matrix()
                val deform2 = 20f
                val src2 = floatArrayOf(
                    0f,
                    0f,
                    bitmap2.width.toFloat(),
                    0f,
                    bitmap2.width.toFloat(),
                    bitmap2.height.toFloat(),
                    0f,
                    bitmap2.height.toFloat()
                )
                val dst2 = floatArrayOf(
                    0f,
                    0f,
                    bitmap2.width - deform2,
                    deform2,
                    bitmap2.width - deform2,
                    bitmap2.height - deform2,
                    0f,
                    bitmap2.height.toFloat()
                )
                matrix2.setPolyToPoly(src2, 0, dst2, 0, src2.size shr 1)

                val bMatrix2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.width, bitmap2.height, matrix2, true)
                canvas.drawBitmap(bMatrix2, matrix2, paint)
            }
            1 -> {
                //先画一个蓝色的矩形
                paint.color = Color.GREEN
                canvas.drawCircle(100f, 100f, 10f, paint)
                canvas.drawCircle(200f, 200f, 10f, paint)
                canvas.drawCircle(300f, 300f, 10f, paint)
                canvas.drawCircle(200f, 300f, 10f, paint)
                canvas.drawCircle(200f, 400f, 10f, paint)
                canvas.drawCircle(300f, 400f, 10f, paint)
                paint.color = Color.TRANSPARENT
                canvas.drawRect(0f, 0f, 200f, 200f, paint)

                mMatrix.setPolyToPoly(
                    floatArrayOf(200f, 200f, 200f, 400f),
                    0,
                    floatArrayOf(200f, 200f, 300f, 400f),
                    0,
                    2
                )

                canvas.setMatrix(mMatrix)
                paint.color = Color.TRANSPARENT
                //通过了setPolyToPoly的改变，再画一个红色矩形看有什么变化
                canvas.drawRect(0f, 200f, 200f, 400f, paint)
                paint.color = Color.YELLOW
                //通过了setPolyToPoly的改变，再画一个红色矩形看有什么变化
                canvas.drawCircle(100f, 100f, 10f, paint)
                canvas.drawCircle(200f, 200f, 10f, paint)
                canvas.drawCircle(300f, 300f, 10f, paint)
                canvas.drawCircle(200f, 300f, 10f, paint)
                canvas.drawCircle(200f, 400f, 10f, paint)
                canvas.drawCircle(300f, 400f, 10f, paint)
            }
            else -> {
            }
        }
    }
}