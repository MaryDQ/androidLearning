package com.mlx.administrator.myapplication.openGL.lesson1

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LessonOneActivity : AppCompatActivity() {

    private var glSurfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //创建一个GLSurfaceView
        /*
        * 1.它为OpenGL提供一个专用的着色线程，因此主线程不会堵塞
        * 2.它支持连续或按需渲染
        * 3.它使用EGL（OpenGL和底层系统窗口之间的接口）来处理屏幕设置
        * */
        glSurfaceView = GLSurfaceView(this)

        //判断设备是否支持OpenGL2
        if (checkSupportOpenGlVersion()) {
            glSurfaceView?.setEGLContextClientVersion(2)
            //设置渲染器
            glSurfaceView?.setRenderer(LessonOneRender())

        } else {
            //这里也可以配置OpenGL 1，创建兼容的渲染器即可
            return
        }

        setContentView(glSurfaceView)
    }

    private fun checkSupportOpenGlVersion(): Boolean {
        //获得一个activityManager的对象，使得我们可以和全局系统状态交互
        //使用actManager获取设备的信息，用来判断是否支持OpenGL ES2
        val actManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = actManager.deviceConfigurationInfo
        return configurationInfo.reqGlEsVersion >= 0x20000
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        //暂停glSurfaceView的渲染线程
        glSurfaceView?.onPause()
    }


}
