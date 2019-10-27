package com.mlx.administrator.myapplication.openGL.lesson1

import android.opengl.GLSurfaceView
import android.opengl.GLU
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.CameraX.init
import com.mlx.administrator.myapplication.R
import kotlinx.android.synthetic.main.activity_test_open_gl.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TestOpenGlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_open_gl)
        initViewAndData()
    }

    private fun initViewAndData() {
        glSfView.setRenderer(GLRender())
        glSfView.onPause()
        glSfView.onResume()
    }

    inner class GLRender : GLSurfaceView.Renderer {

        val mVertices=FloatBufferUtil.getCubeVertexs()
        val pointCount=4
        fun drawCube(gl: GL10?){
            //glEnableClientState用来启用顶点开关，代码设计绘制前需要打开，绘制完毕以后，必须调用glDisableClientState，关闭顶点
            gl?.glEnableClientState(GL10.GL_VERTEX_ARRAY)
            for (buffer in mVertices){
                //glVertexPointer用来将三维物体的顶点坐标传递给OpenGL管道，第一个参数代表所代表顶点的坐标维度，几维度就写几
                //第二个参数代表顶点数据类型，有float，short,etc
                //第三个参数代表顶点之间的间隔，通常取值为0，代表这些顶点是连续的
                //第四个参数代表顶点的具体坐标集合
                gl?.glVertexPointer(3, GL10.GL_FLOAT, 0, buffer)
                //glDrawArrays用来绘制顶点之间的点、线、面
                //第一个参数代表绘制的模式，GL_LINE_LOOP就是代表绘制顶点之间的线，并且首位的点事闭合的，其他几个模式自行查阅
                //第二个参数代表绘制的起始顶点，一般从0开始
                //第三个参数代表绘制的顶点数量，一般都是传进去几个就是几个
                gl?.glDrawArrays(GL10.GL_LINE_LOOP,0,pointCount)
            }
            gl?.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        }

        fun drawBall(gl: GL10?){
            //glEnableClientState用来启用顶点开关，代码设计绘制前需要打开，绘制完毕以后，必须调用glDisableClientState，关闭顶点
            gl?.glEnableClientState(GL10.GL_VERTEX_ARRAY)
            for (buffer in mVertices){
                //glVertexPointer用来将三维物体的顶点坐标传递给OpenGL管道，第一个参数代表所代表顶点的坐标维度，几维度就写几
                //第二个参数代表顶点数据类型，有float，short,etc
                //第三个参数代表顶点之间的间隔，通常取值为0，代表这些顶点是连续的
                //第四个参数代表顶点的具体坐标集合
                gl?.glVertexPointer(3, GL10.GL_FLOAT, 0, buffer)
                //glDrawArrays用来绘制顶点之间的点、线、面
                //第一个参数代表绘制的模式，GL_LINE_LOOP就是代表绘制顶点之间的线，并且首位的点事闭合的，其他几个模式自行查阅
                //第二个参数代表绘制的起始顶点，一般从0开始
                //第三个参数代表绘制的顶点数量，一般都是传进去几个就是几个
                gl?.glDrawArrays(GL10.GL_LINE_LOOP,0,pointCount)
            }
            gl?.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        }

        override fun onDrawFrame(gl: GL10?) {
            gl?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
            gl?.glLoadIdentity()
            //设置画笔颜色
            gl?.glColor4f(0f,0f,1f,1f)
            //GLU.gluLookAt用来设置眼睛位置、目标物体位置、头顶方向
            GLU.gluLookAt(gl,10f,8f,6f,0f,0f,0f,0f,1f,0f)

            drawCube(gl)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            //glViewport方法的作用就是设置画面输出的具体大小和位置，第一个和第二个参数代表输出画面左上角的坐标，
            //第三第四个参数代表画面的宽度和高度
            gl?.glViewport(0,0,width,height)
            //glMatrixMode就是根据模式来重置之前的参数设置，不同模式对应不同的参数设置，调用此方法后，需要立即调用glLoadIdentity生效
            //其中GL10.GL_PROJECTION对应的参数设置有：gluPerspective（调整相机参数）、glFrustumf（调整透视投影）、glOrtheOf（调整正投影）
            gl?.glMatrixMode(GL10.GL_PROJECTION)
            gl?.glLoadIdentity()
            //GLU.gluPerspective方法设置相机参数，第二个参数代表焦距的角度，第三个参数代表相机的画面比例
            //第四个参数表示相机看到的最近距离，第五个参数代表相机看到的最远距离
            GLU.gluPerspective(gl,40.0f, width.toFloat()/height,0.1f,20.0f)
            //GL10.GL_MODELVIEW对应位置变换的相关方法
            gl?.glMatrixMode(GL10.GL_MODELVIEW)
            gl?.glLoadIdentity()



        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            //glClearColor就是设置surface的绘制背景的方法
            gl?.glClearColor(1.0f,1f,1f,1.0f)
            //glShadeModel就是设置surface阴影模式的方法
            gl?.glShadeModel(GL10.GL_SMOOTH)

//            gl?.glEnable(GL10.GL_DEPTH_TEST)

        }

    }
}
