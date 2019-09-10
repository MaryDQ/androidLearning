package com.mlx.administrator.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mlx.administrator.myapplication.recycler.AbstractSimpleAdapter
import com.mlx.administrator.myapplication.recycler.ViewHolder
import kotlinx.android.synthetic.main.activity_test_rcv.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_rcv)
        initData()
    }

    private var scheduledExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(2)

    private var list = ArrayList<String>()

    private var mAdapter: AbstractSimpleAdapter<String>? = null
    private fun initData() {
        list.add("0")

        mAdapter = object : AbstractSimpleAdapter<String>(this, list, R.layout.item_phone_detail_item) {
            override fun onBindViewHolder(holder: ViewHolder?, data: String?, curPosition: Int) {
                holder!!.getView<TextView>(R.id.tvName).text = data
            }
        }

        rvTest.layoutManager = LinearLayoutManager(this)
        rvTest.adapter = mAdapter

        initTimer()

        btnAddOnePercent.setOnClickListener {
//            i += 0.01f
//            cbv.setRatio(i)
//            btnAddOnePercent.text = "$i"
        }
    }

    private var i = 0.01f

    private fun initTimer() {
        scheduledExecutorService.scheduleAtFixedRate({
            Log.e("dididi", "${Thread.currentThread()}")
            try {
                list[0] = (list[0].toInt() + 1).toString()
                Handler(Looper.getMainLooper()).post {
                    mAdapter?.notifyItemChanged(0)
                }
            } catch (e: Exception) {
                Log.e("dididi", "error:${e.message}")
            }
        }, 1, 1L, TimeUnit.SECONDS)
    }

//    var arrayFloatArray: FloatArray? = null
//
//    fun getFloatValues(): FloatArray {
//        var text01 = et1.text.toString().toFloat()
//        var text02 = et2.text.toString().toFloat()
//        var text03 = et3.text.toString().toFloat()
//        var text04 = et4.text.toString().toFloat()
//        var text05 = et5.text.toString().toFloat()
//        var text06 = et6.text.toString().toFloat()
//        var text07 = et7.text.toString().toFloat()
//        var text08 = et8.text.toString().toFloat()
//        var text09 = et9.text.toString().toFloat()
//
//        return floatArrayOf(
//            text01, text02, text03,
//            text04, text05, text06,
//            text07, text08, text09
//        )
//    }
//
//    private fun getMatrix(): android.graphics.Matrix {
//        var text01 = et1.text.toString().toFloat()
//        var text02 = et2.text.toString().toFloat()
//        var text03 = et3.text.toString().toFloat()
//        var text04 = et4.text.toString().toFloat()
//        var text05 = et5.text.toString().toFloat()
//        var text06 = et6.text.toString().toFloat()
//        var text07 = et7.text.toString().toFloat()
//        var text08 = et8.text.toString().toFloat()
//        var text09 = et9.text.toString().toFloat()
//        val matrix = android.graphics.Matrix()
//
//
//        arrayFloatArray = floatArrayOf(
//            text01, text02, text03,
//            text04, text05, text06,
//            text07, text08, text09
//        )
//
//
//        matrix.setValues(
//            arrayFloatArray
//        )
//        return matrix
//    }
//
//    private fun initData() {
////        btnDoMatrix.setOnClickListener {
////            //            mpv.setMartixFromOut(matrix = getMatrix())
//////            mpv.setMatrixValues(getFloatValues())
////            startActivity(Intent(this,CameraXActivity::class.java))
////        }
//    }
}
