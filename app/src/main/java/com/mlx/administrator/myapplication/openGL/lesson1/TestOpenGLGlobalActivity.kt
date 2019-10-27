package com.mlx.administrator.myapplication.openGL.lesson1

import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.opengl.GLUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.mlx.administrator.myapplication.R
import kotlinx.android.synthetic.main.activity_test_open_glglobal.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TestOpenGLGlobalActivity : AppCompatActivity() {

    private val styleArray = arrayOf("东半球", "西半球", "北半球", "南半球", "转动地球仪")
    val mBitmap by lazy { BitmapFactory.decodeResource(resources, R.mipmap.earth3) }
    val mVertices by lazy { FloatBufferUtil.getBallVertices(divide, radius) }
    val divide = 20
    val radius = 4
    val mTextureCoords by lazy { FloatBufferUtil.getTextureCoords(divide) }
    // 旋转角度
    private var mAngle = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_open_glglobal)
        initStyleSpinner()
        glSfGlobalView.setRenderer(Render())
    }


    // 初始化样式下拉框
    private fun initStyleSpinner() {
        val styleAdapter = ArrayAdapter<String>(
            this,
            R.layout.item_select, styleArray
        )
        spStyle.prompt = "请选择球体贴图样式"
        spStyle.adapter = styleAdapter
        spStyle.onItemSelectedListener = StyleSelectedListener()
        spStyle.setSelection(0)
    }

    private var mType: Int = 0 // 地球仪的类型

    internal inner class StyleSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(arg0: AdapterView<*>, arg1: View, arg2: Int, arg3: Long) {
            mType = arg2
        }

        override fun onNothingSelected(arg0: AdapterView<*>) {}
    }


    private inner class Render : GLSurfaceView.Renderer {
        override fun onDrawFrame(gl: GL10?) {
            // 清除屏幕和深度缓存
            gl?.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
            // 重置当前的模型观察矩阵
            gl?.glLoadIdentity()

            when (mType) {
                //0 or 4 代表东半球
                0 -> {
                    GLU.gluLookAt(gl, 0f, 0f, 70f, 0f, 0f, 0f, 0f, 1f, 0f)
                }
                4 -> {
                    GLU.gluLookAt(gl, 0f, 0f, 70f, 0f, 0f, 0f, 0f, 1f, 0f)
                }
                //1代表西半球
                1 -> {
                    GLU.gluLookAt(gl, 0f, 0f, -70f, 0f, 0f, 0f, 0f, 1f, 0f)
                }
                //2代表北半球
                2 -> {
                    GLU.gluLookAt(gl, 0f, 70f, 0f, 0f, 0f, 0f, 1f, 0f, 0f)
                }
                //3代表南半球
                3 -> {
                    GLU.gluLookAt(gl, 0f, -70f, 70f, 0f, 0f, 0f, 1f, 0f, 0f)
                }
            }
            if (mType == 4) {
                //设置旋转角度，转动地球仪
                gl?.glRotatef(mAngle.toFloat(), 0f, 1f, 0f)
                mAngle--
            } else {
                mAngle = 0
            }
            drawGlobal(gl)
        }

        private fun drawGlobal(gl: GL10?) {
            gl?.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
            gl?.glEnableClientState(GL10.GL_VERTEX_ARRAY)
            for (i in 0..divide) {
                // 将顶点坐标传给 OpenGL 管道
                gl?.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertices[i])
                // 声明纹理点坐标
                gl?.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureCoords[i])
                // GL_LINE_STRIP只绘制线条，GL_TRIANGLE_STRIP才是画三角形的面
                gl?.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, divide * 2 + 2)
            }
            gl?.glDisableClientState(GL10.GL_VERTEX_ARRAY)
            gl?.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            gl?.glViewport(0, 0, width, height)

            gl?.glMatrixMode(GL10.GL_PROJECTION)
            gl?.glLoadIdentity()

            GLU.gluPerspective(gl, 8f, width.toFloat() / height, 0.1f, 100f)

            gl?.glMatrixMode(GL10.GL_MODELVIEW)
            gl?.glLoadIdentity()
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            //设置白色背景
            gl?.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
            //启用阴影平滑
            gl?.glShadeModel(GL10.GL_SMOOTH)

            //glEnable用来开启某种功能
            //其中GL_DEPTH_TEST开启后，OpenGL只会在离观察者中间没有其他像素点遮挡的像素点上进行绘制
            //其他有遮挡的像素点不会绘制
            //绘制三维图形时最好开启，这样会比较真实
            //此外还可以开启以下功能
            //开启灯光照效果
            //gl?.glEnable(GL10.GL_LIGHTING)
            //启用光源0
            //gl?.glEnable(GL10.GL_LIGHT0)
            //启用颜色追踪
            //gl?.glEnable(GL10.GL_COLOR_MATERIAL)
            gl?.glEnable(GL10.GL_DEPTH_TEST)
            //GL_TEXTURE_2D启用纹理功能，启用之后才能往上面贴图
            gl?.glEnable(GL10.GL_TEXTURE_2D)

            //以下3行代码用来分配纹理编号，并绑定给OpenGL
            //设计就是如此，非常不好理解
            //使用OpenGL库创建一个材质(Texture),首先要获取一个材质编号(保存在textures中)
            val textures = IntArray(1)
            gl?.glGenTextures(1, textures, 0)
            //通知OpenGL使用这个Texture材质编号
            gl?.glBindTexture(GL10.GL_TEXTURE_2D, textures[0])
            //用来渲染的Texture可能要比渲染的区域大或者小
            //这个时候就需要自己设置这两种情况对应的图像设置模式了
            //常用的模式有2种，一种是清晰的，另一种是模糊的，分别对应GL10.GL_NEAREST和GL10.GL_LINEAR
            gl?.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_NEAREST.toFloat()
            )
            gl?.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR.toFloat()
            )
            //当定义的材质坐标点超过UV坐标定义的大小(0,0到1,1--OpenGL纹理的坐标原点在手机屏幕的左下方，切记切记)
            /**需要告诉OpenGL如何绘制这些超出坐标系的点（这里很是不理解，回头重点排查）**/
            //GL_TEXTURE_WRAP_S代表水平方向，S的英文单词是啥没记，可以用水平的拼音来记
            //那么另外一个GL_TEXTURE_WRAP_T，就是对应了垂直方向
            //GL_REPEAT表示重复渲染，GL_CLAMP_TO_EDGE表示只沿着边线绘制一次
            gl?.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_CLAMP_TO_EDGE.toFloat()
            )
            gl?.glTexParameterf(
                GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_CLAMP_TO_EDGE.toFloat()
            )
            //将位图和Texture纹理绑定起来，位图就是具体的衣服
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0)
        }

    }
}
