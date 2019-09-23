package com.mlx.administrator.myapplication.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable
import androidx.camera.core.CameraX.init

class FingerSignView@JvmOverloads constructor(
    context: Context, @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    var gestureDetector:GestureDetector?=null
    var mStrokeWidth=5f
    var mPaint:Paint
    var mPath: Path
    init {
        mPaint= Paint()
        mPaint.color = Color.parseColor("#ff0078")
        mPaint.isAntiAlias=true
        mPaint.strokeWidth=mStrokeWidth
        mPaint.style=Paint.Style.STROKE
        mPath= Path()
        initGestureDetector()
    }

    var lastDownX=0f
    var lastDownY=0f

    private fun initGestureDetector() {
        gestureDetector=object : GestureDetector(context,object :OnGestureListener{
            override fun onShowPress(p0: MotionEvent?) {
                lastDownX=p0!!.rawX
                lastDownY=p0!!.rawY
            }

            override fun onSingleTapUp(p0: MotionEvent?): Boolean {

                invalidate()
                return true
            }

            override fun onDown(p0: MotionEvent?): Boolean {
                mPath.moveTo(p0!!.rawX,p0.rawY)
                mPath.reset()
                return true
            }

            override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
                return true
            }

            override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
                //滑动
                mPath.quadTo(lastDownX,lastDownY,p1!!.rawX,p1.rawY)
                invalidate()
                return true
            }

            override fun onLongPress(p0: MotionEvent?) {

            }

        }) {

        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mPath,mPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return gestureDetector!!.onTouchEvent(event)
    }

}