package com.mlx.administrator.myapplication.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.mlx.administrator.myapplication.utils.BezierUtil

class QuadBezierView : View {

    /**
     * 起点坐标
     */
    private var mStratPointX = 0.0f
    private var mStartPointY = 0.0f

    /**
     * 终点坐标
     */
    private var mEndPointX = 0.0f
    private var mEndPointY = 0.0f

    /**
     * 控制点坐标
     */
    private var mCtrlPointX = 0.0f
    private var mCtrlPointY = 0.0f

    private var mPath: Path? = null

    /**
     * 移动坐标
     */
    private var mMovePointX = 0.0f
    private var mMovePointY = 0.0f

    /**
     * 画曲线所用的画笔
     */
    private var mPaintBezier: Paint? = null

    /**
     * 画辅助线所用的画笔
     */
    private var mPaintCtrl: Paint? = null

    /**
     * 绘画文字的画笔
     */
    private var mPaintText: Paint? = null

    /**
     * 绘制运动圆圈的画笔
     */
    private var mPaintCircle: Paint? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initData()
    }

    private fun initData() {
        //初始化画笔
        mPaintBezier = Paint(Paint.ANTI_ALIAS_FLAG)
        //划线的宽度
        mPaintBezier?.strokeWidth = 3f
        //画笔的类型，这里是实线
        mPaintBezier?.style = Paint.Style.FILL

        mPaintCtrl = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintCtrl?.strokeWidth = 1f
        mPaintCtrl?.style = Paint.Style.STROKE

        mPaintText = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaintText?.style = Paint.Style.STROKE
        mPaintText?.textSize = 20f

        mPaintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    /**
     * 在每次View的size变化时，设定曲线的起点、终点以及控制点，并绘制曲线
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mStratPointX = w / 10f
        mStartPointY = h / 2f - 200

        mEndPointX = w / 10f * 9
        mEndPointY = h / 2f - 200

        mCtrlPointX = w / 2f
        mCtrlPointY = h / 2f - 300

        mMovePointX = 0f
        mMovePointY = 0f

        mPath = Path()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //绘制曲线
        mPath?.reset()
        mPath?.moveTo(mStratPointX, mStartPointY)
        mPath?.quadTo(mCtrlPointX, mCtrlPointY, mEndPointX, mEndPointY)
//        mPath?.cubicTo(mStratPointX,mStartPointY,mCtrlPointX,mCtrlPointY,mEndPointX,mEndPointY)
        canvas?.drawPath(mPath!!, mPaintBezier!!)

        //绘制起点，终点，控制点
        canvas?.drawPoint(mStratPointX, mStartPointY, mPaintCtrl!!)
        canvas?.drawPoint(mEndPointX, mEndPointY, mPaintCtrl!!)
        canvas?.drawPoint(mCtrlPointX, mCtrlPointY, mPaintCtrl!!)

        //加上文字注解
        canvas?.drawText("起点", mStratPointX, mStartPointY, mPaintText!!)
        canvas?.drawText("终点", mEndPointX, mEndPointY, mPaintText!!)
        canvas?.drawText("控制点", mCtrlPointX, mCtrlPointY, mPaintText!!)

        //绘制辅助线
        canvas?.drawLine(mStratPointX, mStartPointY, mCtrlPointX, mCtrlPointY, mPaintCtrl!!)
        canvas?.drawLine(mCtrlPointX, mCtrlPointY, mEndPointX, mEndPointY, mPaintCtrl!!)

        //绘制圆形
        canvas?.drawCircle(mMovePointX, mMovePointY, 20f, mPaintCircle!!)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_MOVE -> {
                mCtrlPointX = event.x
                mCtrlPointY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                mCtrlPointX = event.x
                mCtrlPointY = event.y
                val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
                valueAnimator.setDuration(2_000L).addUpdateListener { p0 ->
                    var t: Float = p0!!.animatedValue as Float
                    var point = BezierUtil.CalculateBezierPointForQuadratic(
                        t,
                        PointF(mStratPointX, mStartPointY),
                        PointF(mCtrlPointX, mCtrlPointY),
                        PointF(mEndPointX, mEndPointY)
                    )
                    mMovePointX = point.x
                    mMovePointY = point.y
                    invalidate()
                }
                valueAnimator.interpolator = AccelerateInterpolator()
                valueAnimator.start()
            }
        }
        return true
    }
}