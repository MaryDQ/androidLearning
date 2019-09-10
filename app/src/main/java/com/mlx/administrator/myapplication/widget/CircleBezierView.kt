package com.mlx.administrator.myapplication.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.Nullable
import com.mlx.administrator.myapplication.widget.base.BaseView
import java.util.*

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/9
 * @description 用贝塞尔曲线绘制圆的过程
 */
class CircleBezierView : BaseView {

    // 圆的中心点
    private var mCenterPoint: PointF? = null
    // 圆半径
    private var mRadius: Float = 0.toFloat()

    // 控制点列表，顺序为：右上、右下、左下、左上
    private var mControlPointList: MutableList<PointF>? = null

    // 控制点占半径的比例
    private var mRatio: Float = 0.toFloat()

    // 圆的路径
    private var mPath: Path? = null

    // 绘制贝塞尔曲线的画笔
    private var mPaint: Paint? = null
    // 绘制圆的画笔
    private var mCirclePaint: Paint? = null
    // 绘制控制线的画笔
    private var mLinePaint: Paint? = null

    // 控制线的颜色
    private var mLineColor: IntArray? = null

    // 线的宽度
    private var LINE_WIDTH: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    /**
     * 设置比例
     *
     * @param ratio 比例，0-1
     */
    fun setRatio(ratio: Float) {
        this.mRatio = ratio
        calculateControlPoint()
        invalidate()
    }

    protected override fun init(context: Context) {
        val width = context.resources.displayMetrics.widthPixels
        mRadius = (width / 3).toFloat()

        LINE_WIDTH = dpToPx(2f)

        mCenterPoint = PointF(0f, 0f)

        mControlPointList = ArrayList()

        mPath = Path()

        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = Color.parseColor("#1296db")

        mCirclePaint = Paint()
        mCirclePaint!!.isAntiAlias = true
        mCirclePaint!!.style = Paint.Style.STROKE
        mCirclePaint!!.strokeWidth = LINE_WIDTH.toFloat()
        mCirclePaint!!.color = Color.RED

        mRatio = 0.55f

        mLineColor = IntArray(4)
        mLineColor!![0] = Color.parseColor("#f4ea2a")    //黄色
        mLineColor!![1] = Color.parseColor("#1afa29")    //绿色
        mLineColor!![2] = Color.parseColor("#efb336")    //橙色
        mLineColor!![3] = Color.parseColor("#e89abe")    //粉色

        mLinePaint = Paint()
        mLinePaint!!.isAntiAlias = true
        mLinePaint!!.style = Paint.Style.STROKE
        mLinePaint!!.strokeWidth = dpToPx(2f).toFloat()

        calculateControlPoint()
    }

    protected override fun onDraw(canvas: Canvas) {
        drawCoordinate(canvas)

        canvas.translate(mWidth / 2, mHeight / 2)

        mPath!!.reset()

        for (i in 0..3) {
            if (i == 0) {
                mPath!!.moveTo(mControlPointList!![i * 3].x, mControlPointList!![i * 3].y)
            } else {
                mPath!!.lineTo(mControlPointList!![i * 3].x, mControlPointList!![i * 3].y)
            }

            val endPointIndex: Int
            if (i == 3) {
                endPointIndex = 0
            } else {
                endPointIndex = i * 3 + 3
            }

            mPath!!.cubicTo(
                mControlPointList!![i * 3 + 1].x, mControlPointList!![i * 3 + 1].y,
                mControlPointList!![i * 3 + 2].x, mControlPointList!![i * 3 + 2].y,
                mControlPointList!![endPointIndex].x, mControlPointList!![endPointIndex].y
            )
        }

        // 绘制贝塞尔曲线
        canvas.drawPath(mPath!!, mPaint!!)

        // 绘制圆
        canvas.drawCircle(mCenterPoint!!.x, mCenterPoint!!.y, mRadius, mCirclePaint!!)

        // 绘制控制线
        for (i in mControlPointList!!.indices) {
            // 设置颜色
            mLinePaint!!.color = mLineColor!![i / 3]

            val endPointIndex = if (i == mControlPointList!!.size - 1) 0 else i + 1

            canvas.drawLine(
                mControlPointList!![i].x,
                mControlPointList!![i].y,
                mControlPointList!![endPointIndex].x,
                mControlPointList!![endPointIndex].y,
                mLinePaint!!
            )
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

    /**
     * 计算圆的控制点
     */
    private fun calculateControlPoint() {
        // 计算 中间控制点到端点的距离
        val controlWidth = mRatio * mRadius

        mControlPointList!!.clear()

        // 右上
        mControlPointList!!.add(PointF(0f, -mRadius))
        mControlPointList!!.add(PointF(controlWidth, -mRadius))
        mControlPointList!!.add(PointF(mRadius, -controlWidth))

        // 右下
        mControlPointList!!.add(PointF(mRadius, 0f))
        mControlPointList!!.add(PointF(mRadius, controlWidth))
        mControlPointList!!.add(PointF(controlWidth, mRadius))

        // 左下
        mControlPointList!!.add(PointF(0f, mRadius))
        mControlPointList!!.add(PointF(-controlWidth, mRadius))
        mControlPointList!!.add(PointF(-mRadius, controlWidth))
        // 左上
        mControlPointList!!.add(PointF(-mRadius, 0f))
        mControlPointList!!.add(PointF(-mRadius, -controlWidth))
        mControlPointList!!.add(PointF(-controlWidth, -mRadius))

    }

}
