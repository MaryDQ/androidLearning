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

        glSurfaceView = GLSurfaceView(this)

        if (checkSupportOpenGlVersion()) {
            glSurfaceView?.setEGLContextClientVersion(2)
            //设置渲染器
            glSurfaceView?.setRenderer(LessonOneRender())

        } else {
            return
        }

        setContentView(glSurfaceView)
    }

    private fun checkSupportOpenGlVersion(): Boolean {
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
        glSurfaceView?.onPause()
    }


}
