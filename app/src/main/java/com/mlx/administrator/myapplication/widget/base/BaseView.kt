package com.mlx.administrator.myapplication.widget.base

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable

abstract class BaseView @JvmOverloads constructor(
    context: Context, @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    protected var TAG = this.javaClass.simpleName

    // 坐标画笔
    private var mCoordinatePaint: Paint? = null
    // 网格画笔
    private var mGridPaint: Paint? = null
    // 写字画笔
    private var mTextPaint: Paint? = null

    // 坐标颜色
    private var mCoordinateColor: Int = 0
    private var mGridColor: Int = 0

    // 网格宽度 50px
    private val mGridWidth = 50

    // 坐标线宽度
    private val mCoordinateLineWidth = 2.5f
    // 网格宽度
    private val mGridLineWidth = 1f
    // 字体大小
    private var mTextSize: Float = 0.toFloat()

    // 标柱的高度
    private val mCoordinateFlagHeight = 8f

    private var _isInit: Boolean = false
    protected var mWidth: Float = 0.toFloat()
    protected var mHeight: Float = 0.toFloat()

    private var mStatusBarHeight: Int = 0

    init {
        initCoordinate(context)
        init(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!_isInit) {
            _isInit = true

            mWidth = measuredWidth.toFloat()
            mHeight = (measuredHeight + mStatusBarHeight).toFloat()
        }
    }

    protected fun initCoordinate(context: Context) {
        mCoordinateColor = Color.BLACK
        mGridColor = Color.LTGRAY

        mStatusBarHeight = getStatusBarHeight(context)

        mTextSize = sp2px(context, 10f).toFloat()

        mCoordinatePaint = Paint()
        mCoordinatePaint!!.isAntiAlias = true
        mCoordinatePaint!!.color = mCoordinateColor
        mCoordinatePaint!!.strokeWidth = mCoordinateLineWidth

        mGridPaint = Paint()
        mGridPaint!!.isAntiAlias = true
        mGridPaint!!.color = mGridColor
        mGridPaint!!.strokeWidth = mGridLineWidth

        mTextPaint = Paint()
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.color = mCoordinateColor
        mTextPaint!!.textAlign = Paint.Align.CENTER
        mTextPaint!!.textSize = mTextSize
    }

    protected abstract fun init(context: Context)

    /**
     * 画坐标和网格，以画布中心点为原点
     *
     * @param canvas 画布
     */
    protected fun drawCoordinate(canvas: Canvas) {

        val halfWidth = mWidth / 2
        val halfHeight = mHeight / 2

        // 画网格
        canvas.save()
        canvas.translate(halfWidth, halfHeight)
        var curWidth = mGridWidth
        // 画横线
        while (curWidth < halfWidth + mGridWidth) {

            // 向右画
            canvas.drawLine(curWidth.toFloat(), -halfHeight, curWidth.toFloat(), halfHeight, mGridPaint!!)
            // 向左画
            canvas.drawLine((-curWidth).toFloat(), -halfHeight, (-curWidth).toFloat(), halfHeight, mGridPaint!!)

            // 画标柱
            canvas.drawLine(curWidth.toFloat(), 0f, curWidth.toFloat(), -mCoordinateFlagHeight, mCoordinatePaint!!)
            canvas.drawLine(
                (-curWidth).toFloat(),
                0f,
                (-curWidth).toFloat(),
                -mCoordinateFlagHeight,
                mCoordinatePaint!!
            )

            // 标柱宽度（每两个画一个）
            if (curWidth % (mGridWidth * 2) == 0) {
                canvas.drawText(curWidth.toString() + "", curWidth.toFloat(), mTextSize * 1.5f, mTextPaint!!)
                canvas.drawText((-curWidth).toString() + "", (-curWidth).toFloat(), mTextSize * 1.5f, mTextPaint!!)
            }

            curWidth += mGridWidth
        }

        var curHeight = mGridWidth
        // 画竖线
        while (curHeight < halfHeight + mGridWidth) {

            // 向右画
            canvas.drawLine(-halfWidth, curHeight.toFloat(), halfWidth, curHeight.toFloat(), mGridPaint!!)
            // 向左画
            canvas.drawLine(-halfWidth, (-curHeight).toFloat(), halfWidth, (-curHeight).toFloat(), mGridPaint!!)

            // 画标柱
            canvas.drawLine(0f, curHeight.toFloat(), mCoordinateFlagHeight, curHeight.toFloat(), mCoordinatePaint!!)
            canvas.drawLine(
                0f,
                (-curHeight).toFloat(),
                mCoordinateFlagHeight,
                (-curHeight).toFloat(),
                mCoordinatePaint!!
            )

            // 标柱宽度（每两个画一个）
            if (curHeight % (mGridWidth * 2) == 0) {
                canvas.drawText(curHeight.toString() + "", -mTextSize * 2, curHeight + mTextSize / 2, mTextPaint!!)
                canvas.drawText((-curHeight).toString() + "", -mTextSize * 2, -curHeight + mTextSize / 2, mTextPaint!!)
            }

            curHeight += mGridWidth
        }
        canvas.restore()

        // 画 x，y 轴
        canvas.drawLine(halfWidth, 0f, halfWidth, mHeight, mCoordinatePaint!!)
        canvas.drawLine(0f, halfHeight, mWidth, halfHeight, mCoordinatePaint!!)

    }

    protected fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 转换 dp 至 px
     *
     * @param dp dp像素
     * @return
     */
    protected fun dpToPx(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        return (dp * metrics.density + 0.5f).toInt()
    }

    protected fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}
