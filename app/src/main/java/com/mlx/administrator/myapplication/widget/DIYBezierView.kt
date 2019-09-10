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
 * @description
 */
class DIYBezierView : BaseView {

    // 圆的中心点
    private var mCenterPoint: PointF? = null
    // 圆半径
    private var mRadius: Float = 0.toFloat()

    // 控制点列表，顺序为：右上、右下、左下、左上
    private var mControlPointList: MutableList<PointF>? = null

    // 选中的点集合，受 status 影响
    private val mCurSelectPointList = ArrayList<PointF>()
    // 选中的点
    private var mSelPoint: PointF? = null

    // 控制点占半径的比例
    private var mRatio: Float = 0.toFloat()

    private var mPath: Path? = null

    private var mControlPath: Path? = null

    private var mPaint: Paint? = null
    private var mCirclePaint: Paint? = null
    private var mControlPaint: Paint? = null

    // 有效触碰的范围
    private var mTouchRegionWidth: Int = 0

    // 线的宽度
    private var LINE_WIDTH: Int = 0
    // 控制点的半径
    private var POINT_RADIO_WIDTH: Int = 0
    // 选中控制点的半径
    private var SEL_POINT_RADIO_WIDTH: Int = 0

    // 拽动状态
    private var mStatus: Status? = null

    // 是否显示辅助线
    private var mIsShowHelpLine: Boolean = false

    // 触碰的x轴
    private var mLastX = -1f
    // 触碰的y轴
    private var mLastY = -1f

    /**
     * 获取控制点
     *
     * @return
     */
    val controlPointList: List<PointF>?
        get() = mControlPointList

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

    fun setStatus(status: Status) {
        this.mStatus = status
    }

    fun setIsShowHelpLine(isShowHelpLine: Boolean) {
        this.mIsShowHelpLine = isShowHelpLine
        invalidate()
    }

    protected override fun init(context: Context) {
        val width = context.resources.displayMetrics.widthPixels
        mRadius = (width / 4).toFloat()

        LINE_WIDTH = dpToPx(2f)
        POINT_RADIO_WIDTH = dpToPx(4f)
        SEL_POINT_RADIO_WIDTH = dpToPx(6f)
        mTouchRegionWidth = dpToPx(20f)

        mCenterPoint = PointF(0f, 0f)

        mControlPointList = ArrayList()

        mPath = Path()

        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = Color.parseColor(BEZIER_CIRCLE_COLOR)

        mCirclePaint = Paint()
        mCirclePaint!!.isAntiAlias = true
        mCirclePaint!!.style = Paint.Style.STROKE
        mCirclePaint!!.strokeWidth = LINE_WIDTH.toFloat()
        mCirclePaint!!.color = Color.parseColor(NATIVE_CIRCLE_COLOR)

        mControlPaint = Paint()
        mControlPaint!!.isAntiAlias = true
        mControlPaint!!.style = Paint.Style.STROKE
        mControlPaint!!.strokeWidth = LINE_WIDTH.toFloat()
        mControlPaint!!.color = Color.parseColor(CONTROL_LINE_COLOR)

        mControlPath = Path()

        mStatus = Status.FREE

        mIsShowHelpLine = true

        mRatio = 0.55f

        calculateControlPoint()
    }

    fun reset() {
        calculateControlPoint()
        invalidate()
    }

    protected override fun onDraw(canvas: Canvas) {
        drawCoordinate(canvas)

        canvas.translate(mWidth / 2, mHeight / 2)

        mPath!!.reset()

        // 画圆
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

        // 不需要辅助线，则画完贝塞尔曲线就终止
        if (!mIsShowHelpLine) {
            return
        }

        // 绘制圆
        canvas.drawCircle(mCenterPoint!!.x, mCenterPoint!!.y, mRadius, mCirclePaint!!)

        // 控制基线
        mControlPath!!.reset()
        for (i in 0..3) {
            val startIndex = i * 3
            if (i == 0) {
                mControlPath!!.moveTo(
                    mControlPointList!![mControlPointList!!.size - 1].x,
                    mControlPointList!![mControlPointList!!.size - 1].y
                )
            } else {
                mControlPath!!.moveTo(mControlPointList!![startIndex - 1].x, mControlPointList!![startIndex - 1].y)
            }

            mControlPath!!.lineTo(mControlPointList!![startIndex].x, mControlPointList!![startIndex].y)
            mControlPath!!.lineTo(mControlPointList!![startIndex + 1].x, mControlPointList!![startIndex + 1].y)
        }
        mControlPaint!!.color = Color.parseColor(CONTROL_LINE_COLOR)
        mControlPaint!!.style = Paint.Style.STROKE
        canvas.drawPath(mControlPath!!, mControlPaint!!)

        // 控制点
        mControlPaint!!.style = Paint.Style.FILL
        for (i in mControlPointList!!.indices) {
            val point = mControlPointList!![i]
            val radio: Float
            if (mCurSelectPointList.contains(point)) {      // 绘制选中的点
                mControlPaint!!.color = Color.parseColor(SEL_POINT_COLOR)
                radio = SEL_POINT_RADIO_WIDTH.toFloat()
            } else {        // 绘制为选中的点
                mControlPaint!!.color = Color.parseColor(CONTROL_LINE_COLOR)
                radio = POINT_RADIO_WIDTH.toFloat()
            }
            canvas.drawCircle(point.x, point.y, radio, mControlPaint!!)
        }

        // 如果为三点拽动，将三点连接
        if (mStatus == Status.THREE) {
            if (mCurSelectPointList.size == 1) {
                return
            }
            for (i in 0 until mCurSelectPointList.size - 1) {
                val p1 = mCurSelectPointList[i]
                val p2 = mCurSelectPointList[i + 1]
                mControlPaint!!.color = Color.parseColor(SEL_POINT_COLOR)
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mControlPaint!!)
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        // 触碰的坐标
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (selectControlPoint(x, y)) {
                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                if (mLastX == -1f || mLastY == -1f) {
                    return true
                }

                // 计算偏移值
                var offsetX = x - mLastX
                var offsetY = y - mLastY

                if ((mStatus == Status.MIRROR_DIFF || mStatus == Status.MIRROR_SAME) && mSelPoint != null) {

                    mSelPoint!!.x = mSelPoint!!.x + offsetX
                    mSelPoint!!.y = mSelPoint!!.y + offsetY

                    var otherPoint: PointF? = null
                    for (point in mCurSelectPointList) {
                        if (point !== mSelPoint) {
                            otherPoint = point
                            break
                        }
                    }

                    if (mStatus == Status.MIRROR_DIFF) {
                        offsetX = -offsetX
                        offsetY = -offsetY
                    }

                    if (otherPoint != null) {
                        otherPoint.x = otherPoint.x + offsetX
                        otherPoint.y = otherPoint.y + offsetY
                    }

                } else {
                    // 更新选中
                    for (point in mCurSelectPointList) {
                        point.x = point.x + offsetX
                        point.y = point.y + offsetY
                    }
                }


                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_UP -> {
                mCurSelectPointList.clear()
                mSelPoint = null
                mLastX = -1f
                mLastY = -1f
            }
        }

        invalidate()

        return true
    }

    /**
     * 是否在有效的触碰范围
     *
     * @param x
     * @param y
     * @return true 有选中；false 无选中
     */
    private fun selectControlPoint(x: Float, y: Float): Boolean {

        // 选中的下标
        var selIndex = -1

        for (i in mControlPointList!!.indices) {

            val controlPoint = mControlPointList!![i]

            val resultX = controlPoint.x + mWidth / 2
            val resultY = controlPoint.y + mHeight / 2

            val pointRange = RectF(
                resultX - mTouchRegionWidth,
                resultY - mTouchRegionWidth,
                resultX + mTouchRegionWidth,
                resultY + mTouchRegionWidth
            )

            if (pointRange.contains(x, y)) {
                selIndex = i
                break
            }
        }

        // 如果没有选中的就返回
        if (selIndex == -1) {
            return false
        }

        // 清空之前的选中点
        mCurSelectPointList.clear()

        mSelPoint = mControlPointList!![selIndex]

        when (mStatus) {
            DIYBezierView.Status.FREE  // 任意点拽动
            -> mCurSelectPointList.add(mControlPointList!![selIndex])
            DIYBezierView.Status.THREE // 三点拽动，需要同时选中三个
            -> {

                // 进行整体的偏移下标，便于计算
                val offsetSelIndex = (selIndex + 1) % 12
                val offsetRangeIndex = offsetSelIndex / 3

                if (offsetRangeIndex == 0) {
                    mCurSelectPointList.add(mControlPointList!![11])
                } else {
                    mCurSelectPointList.add(mControlPointList!![offsetRangeIndex * 3 - 1])
                }

                mCurSelectPointList.add(mControlPointList!![offsetRangeIndex * 3])
                mCurSelectPointList.add(mControlPointList!![offsetRangeIndex * 3 + 1])
            }
            // 镜像，需要同时选中两个
            DIYBezierView.Status.MIRROR_DIFF, DIYBezierView.Status.MIRROR_SAME -> if (selIndex == 0 || selIndex == 6) {
                mCurSelectPointList.add(mControlPointList!![0])
                mCurSelectPointList.add(mControlPointList!![6])
            } else {
                mCurSelectPointList.add(mControlPointList!![selIndex])
                mCurSelectPointList.add(mControlPointList!![12 - selIndex])
            }
        }

        return true

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

    enum class Status {
        FREE, // 自由拽动
        THREE, // 三点拽动
        MIRROR_DIFF, // 镜像异向
        MIRROR_SAME
        // 镜像同向
    }

    companion object {

        private val BEZIER_CIRCLE_COLOR = "#20A298"    //绿色
        private val NATIVE_CIRCLE_COLOR = "#F6A010"    //橙色
        private val CONTROL_LINE_COLOR = "#FA3096"     //艳红色
        private val SEL_POINT_COLOR = "#1296db"        //蓝色
    }

}
