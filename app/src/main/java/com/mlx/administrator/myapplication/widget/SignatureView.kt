package com.mlx.administrator.myapplication.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.mlx.administrator.myapplication.R
import java.util.ArrayList

class SignatureView : View {
    private var mPaint: Paint? = null // 声明一个画笔对象
    private var mCanvas: Canvas? = null // 声明一个画布对象
    private var mBitmap: Bitmap? = null // 声明一个位图对象
    private var mPath: Path? = null // 声明一个路径对象
    private var mPaintColor = Color.BLACK // 画笔颜色
    private var mStrokeWidth = 3 // 画笔线宽
    private var mPos = PathPosition() // 路径位置
    private val mPathArray = ArrayList<PathPosition>() // 路径位置队列
    private var mLastX: Float = 0.toFloat()
    private var mLastY: Float = 0.toFloat() // 上次触摸点的横纵坐标

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        if (attrs != null) {
//            // 根据SignatureView的属性定义，从布局文件中获取属性数组描述
//            val attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.SignatureView)
//            // 根据属性描述定义，获取布局文件中的画笔颜色
//            mPaintColor = attrArray.getColor(R.styleable.SignatureView_paint_color, Color.BLACK)
//            // 根据属性描述定义，获取布局文件中的画笔线宽
//            mStrokeWidth = attrArray.getInt(R.styleable.SignatureView_stroke_width, 3)
//            // 回收属性数组描述
//            attrArray.recycle()
//        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initView(measuredWidth, measuredHeight)
    }

    // 初始化视图
    private fun initView(width: Int, height: Int) {
        mPaint = Paint() // 创建新画笔
        mPaint!!.isAntiAlias = true //设置画笔为无锯齿
        mPaint!!.strokeWidth = mStrokeWidth.toFloat() // 设置画笔的线宽
        mPaint!!.style = Paint.Style.STROKE // 设置画笔的类型。STROK表示空心，FILL表示实心
        mPaint!!.color = mPaintColor // 设置画笔的颜色
        mPath = Path() // 创建新路径
        // 开启当前视图的绘图缓存
        isDrawingCacheEnabled = true
        // 创建一个空白位图
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        // 根据空白位图创建画布
        mCanvas = Canvas(mBitmap!!)
        clear()
    }

    // 清空画布
    fun clear() {
        if (mCanvas != null) {
            // 清空路径位置队列
            mPathArray.clear()
            // 给画布设置透明背景
            mCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            invalidate() // 立刻刷新视图
        }
    }

    // 撤销上一次绘制
    fun revoke() {
        if (mPathArray.size > 0) {
            // 移除路径位置队列中的最后一个路径
            mPathArray.removeAt(mPathArray.size - 1)
            // 给画布设置透明背景
            mCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            for (i in mPathArray.indices) {
                val posPath = Path()
                // 移动到下一个坐标点
                posPath.moveTo(mPathArray[i].firstX, mPathArray[i].firstY)
                // 连接上一个坐标点和下一个坐标点
                posPath.quadTo(
                    mPathArray[i].firstX, mPathArray[i].firstY,
                    mPathArray[i].nextX, mPathArray[i].nextY
                )
                // 在画布上绘制指定路径线条
                mCanvas!!.drawPath(posPath, mPaint!!)
            }
            invalidate() // 立刻刷新视图
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 在画布上绘制指定位图
        canvas.drawBitmap(mBitmap!!, 0f, 0f, null)
        // 在画布上绘制指定路径线条
        canvas.drawPath(mPath!!, mPaint!!)
    }

    var gestureDetector:GestureDetector= GestureDetector(context,object :GestureDetector.OnGestureListener{
        override fun onShowPress(p0: MotionEvent?) {

        }

        override fun onSingleTapUp(p0: MotionEvent?): Boolean {
            // 在画布上绘制指定路径线条
            mCanvas!!.drawPath(mPath!!, mPaint!!)
            mPath!!.reset()
            return true
        }

        override fun onDown(p0: MotionEvent?): Boolean {
            // 移动到指定坐标点
            mPath!!.moveTo(p0!!.x, p0.y)
            mPos.firstX = p0.x
            mPos.firstY = p0.y
            return true
        }

        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return true
        }

        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            // 连接上一个坐标点和当前坐标点
            mPath!!.quadTo(mLastX, mLastY, p1!!.x, p1.y)
            mPos.nextX = p1.x
            mPos.nextY = p1.y
            // 往路径位置队列添加路径位置
            mPathArray.add(mPos)
            // 创建新的路径位置
            mPos = PathPosition()
            mPos.firstX = p1.x
            mPos.firstY = p1.y
            return true
        }

        override fun onLongPress(p0: MotionEvent?) {
        }
    })

    // 在发生触摸事件时触发
    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.action) {
//            MotionEvent.ACTION_DOWN // 手指按下
//            -> {
//                // 移动到指定坐标点
//                mPath!!.moveTo(event.x, event.y)
//                mPos.firstX = event.x
//                mPos.firstY = event.y
//            }
//            MotionEvent.ACTION_MOVE // 手指移动
//            -> {
//                // 连接上一个坐标点和当前坐标点
//                mPath!!.quadTo(mLastX, mLastY, event.x, event.y)
//                mPos.nextX = event.x
//                mPos.nextY = event.y
//                // 往路径位置队列添加路径位置
//                mPathArray.add(mPos)
//                // 创建新的路径位置
//                mPos = PathPosition()
//                mPos.firstX = event.x
//                mPos.firstY = event.y
//            }
//            MotionEvent.ACTION_UP // 手指松开
//            -> {
//                // 在画布上绘制指定路径线条
//                mCanvas!!.drawPath(mPath!!, mPaint!!)
//                mPath!!.reset()
//            }
//        }
        mLastX = event.x
        mLastY = event.y
        invalidate() // 立刻刷新视图
        return gestureDetector.onTouchEvent(event)
    }

    // 定义一个路径位置实体类，包括当前落点的横纵坐标，以及下个落点的横纵坐标
    private inner class PathPosition {
        var firstX: Float = 0.toFloat()
        var firstY: Float = 0.toFloat()
        var nextX: Float = 0.toFloat()
        var nextY: Float = 0.toFloat()

        init {
            firstX = 0f
            firstY = 0f
            nextX = 0f
            nextY = 0f
        }
    }

    companion object {
        private val TAG = "SignatureView"
    }

}
