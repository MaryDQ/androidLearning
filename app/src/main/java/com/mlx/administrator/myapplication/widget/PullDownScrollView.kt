package com.mlx.administrator.myapplication.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.mlx.administrator.myapplication.R
import com.mlx.administrator.myapplication.utils.Utils
import kotlin.math.absoluteValue


class PullDownScrollView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null) :
    ScrollView(context, attr) {
    private var mOffsetX: Float = 0.toFloat()
    private var mOffsetY: Float = 0.toFloat() // 横纵方向上的偏移
    private var mLastPosX: Float = 0.toFloat()
    private var mLastPosY: Float = 0.toFloat() // 上次落点的横纵坐标
    private val mInterval: Int // 与边缘线的间距阈值

    private var mScrollListener: ScrollListener? = null // 声明一个滚动监听器对象

    init {
//        mInterval = Utils.dip2px(context, 3)
        mInterval = 7
    }

    // 在拦截触摸事件时触发
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val result: Boolean
        when (event.action) {
            MotionEvent.ACTION_DOWN // 手指按下
            -> {
                mOffsetX = 0.0f
                mOffsetY = 0.0f
                mLastPosX = event.x
                mLastPosY = event.y
                result = super.onInterceptTouchEvent(event)
            }
            else // 其余动作，包括手指移动、手指松开等等
            -> {
                val thisPosX = event.x
                val thisPosY = event.y
                mOffsetX += Math.abs(thisPosX - mLastPosX) // x轴偏差
                mOffsetY += Math.abs(thisPosY - mLastPosY) // y轴偏差
                mLastPosX = thisPosX
                mLastPosY = thisPosY
                if (mOffsetX < mInterval && mOffsetY < mInterval) {
                    result = false // false传给表示子控件，此时为点击事件
                } else if (mOffsetX < mOffsetY) {
                    result = true // true表示不传给子控件，此时为垂直滑动
                } else {
                    result = false // false表示传给子控件，此时为水平滑动
                }
            }
        }
        return result
    }

    // 在滚动变更时触发
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        val isScrolledToTop: Boolean
        val isScrolledToBottom: Boolean
        if (scrollY == 0) { // 下拉滚动到顶部
            isScrolledToTop = true
            isScrolledToBottom = false
        } else if (scrollY + height - paddingTop - paddingBottom == getChildAt(0).height) { // 上拉滚动到底部
            isScrolledToBottom = true
            isScrolledToTop = false
        } else { // 未拉到顶部，也未拉到底部
            isScrolledToTop = false
            isScrolledToBottom = false
        }
        if (mScrollListener != null) {
            if (isScrolledToTop) { // 已经滚动到顶部
                // 触发下拉到顶部的事件
                mScrollListener!!.onScrolledToTop()
            } else if (isScrolledToBottom) { // 已经滚动到底部
                // 触发上拉到底部的事件
                mScrollListener!!.onScrolledToBottom()
            }
        }
    }

    // 设置滚动监听器
    fun setScrollListener(listener: ScrollListener) {
        mScrollListener = listener
    }

    // 定义一个滚动监听器接口，用于捕捉到达顶部和到达底部的事件
    interface ScrollListener {
        fun onScrolledToBottom()  // 已经滚动到底部
        fun onScrolledToTop()  // 已经滚动到顶部
    }

}