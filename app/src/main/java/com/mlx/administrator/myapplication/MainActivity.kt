package com.mlx.administrator.myapplication

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
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
            downSomething(qqUrl)
        }
    }

    private var wangzheUrl="https://718e31e8894454e98bf531c997f4e6fb.dd.cdntips.com/imtt.dd.qq.com/16891/apk/306C22A4972B3883976938ED066EDB74.apk?mkey=5d842661b79ed625&f=8935&fsname=com.tencent.tmgp.sgame_1.46.1.18_46011805.apk&csr=1bbd&cip=183.158.240.208&proto=https";
    private val qqUrl="https://eaac524d22b5efded110a28fee901a58.dd.cdntips.com/imtt.dd.qq.com/16891/apk/ADE42BB02016AD9B4E9CCAD6C2DF030A.apk?mkey=5d8424dcb79ed625&f=1806&fsname=com.tencent.mobileqq_8.1.5_1258.apk&csr=1bbd&cip=183.158.240.208&proto=https"

    private fun downSomething(downUrl:String){
        var downloadManager=getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var resource= Uri.parse(downUrl)

        var request=DownloadManager.Request(resource)
        request.setDestinationInExternalPublicDir("Download","nihao.apk")

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE and DownloadManager.Request.NETWORK_WIFI)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle("测试下载中")
        request.setVisibleInDownloadsUi(true)

        downloadManager.enqueue(request)
    }

    private var i = 0.01f

    private fun initTimer() {
        scheduledExecutorService.scheduleAtFixedRate({
            try {
                list[0] = (list[0].toInt() + 1).toString()
                Handler(Looper.getMainLooper()).post {
                    mAdapter?.notifyItemChanged(0)
                }
            } catch (e: Exception) {
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
