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
import com.mlx.administrator.myapplication.R
import com.mlx.administrator.myapplication.utils.Utils

@SuppressLint("ClickableViewAccessibility")
class PullDownRefreshLayout @JvmOverloads constructor(
    private val mContext: Context // 声明一个上下文对象
    , attrs: AttributeSet? = null
) : LinearLayout(mContext, attrs), View.OnTouchListener, PullDownScrollView.ScrollListener {

    companion object {
        private val TAG = "PullDownRefreshLayout"
    }

    private var mCriticalDistance = 0
    private var mLinearLayout: LinearLayout
    private var mLayoutHeight = 0

    private var mOriginX = 0f
    private var mOriginY = 0f

    private var mCurrentHeight = 0f

    private var mScrollView: PullDownScrollView? = null

    init {
        //出发工具栏变色的临界滑动距离
        mCriticalDistance = 120
        //获取默认的下拉刷新头布局
        mLinearLayout = LayoutInflater.from(mContext).inflate(R.layout.drag_drop_header, null) as LinearLayout
        //计算下拉刷新头部布局的高度
        mLayoutHeight = Utils.getRealHeight(mLinearLayout)
        //间隔是负值，表示不但不远离，反而插了进去
        mLinearLayout.setPadding(0, -1 * mLayoutHeight, 0, 0)
        //立刻刷新线性视图
        mLinearLayout.invalidate()
        //把下拉刷新头部布局添加到最前面
        addView(mLinearLayout, 0)
    }


    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onScrolledToBottom() {

    }

    override fun onScrolledToTop() {
        mListener?.pullUp(0.0)
    }


    /***
     * 刷新完毕，恢复原页面
     */
    fun finishRefresh(){
        resumePage()
    }

    /**
     * 添加下级视图时触发，主要是给PullDownScrollView绑定监听
     */
    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is PullDownScrollView) {
            mScrollView=child
            //设置触摸监听器，目的是监控拉动的距离
            mScrollView!!.setOnTouchListener(this)
            //设置滚动监听器，目的是判断是否拉到顶部或者拉到底部
            mScrollView!!.setScrollListener(this)
        }
        super.addView(child, index, params)
    }

    /**
     * 恢复主页面
     */
    private fun resumePage(){
        mLinearLayout.setPadding(0,(-1*mLayoutHeight),0,0)
        //立刻刷新线性视图
        mLinearLayout.invalidate()
        mListener?.showTitle()
    }

    // 声明一个下拉刷新的监听器对象
    private var mListener: PullRefreshListener? = null

    /**
     *  设置下拉刷新监听器
     *  @param listener 监听的listener
     */
    fun setOnRefreshListener(listener: PullRefreshListener) {
        mListener = listener
    }


    interface PullRefreshListener {
        /**
         * 正在上拉
         * @param scale
         */
        fun pullUp(scale: Double)

        /**
         * 正在下拉
         * @param scale
         */
        fun pullDown(scale: Double)

        /**
         * 开始刷新动作
         */
        fun pullRefresh()

        /**
         * 隐藏标题栏
         */
        fun hideTitle()

        /**
         * 显示标题栏
         */
        fun showTitle()
    }

}
