package com.mlx.administrator.myapplication.openGL.lesson1

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class LessonOneRender// X, Y, Z,
// R, G, B, A

// Initialize the buffers.
// X, Y, Z,
// R, G, B, A

// This triangle is white, gray, and black.
// X, Y, Z,
// R, G, B, A

// This triangle is yellow, cyan, and magenta.
/**
 * Initialize the model data.
 */

// Define points for equilateral triangles.

// This triangle is red, green, and blue.
    () : GLSurfaceView.Renderer {

    /** How many bytes per float.  */
    private val mBytesPerFloat = 4

    private val mTriangle1Vertices: FloatBuffer
    private val mTriangle2Vertices: FloatBuffer
    private val mTriangle3Vertices: FloatBuffer

    /** 用于传递变换矩阵.  */
    private var mMVPMatrixHandle: Int = 0

    /** 用于传递model位置信息.  */
    private var mPositionHandle: Int = 0

    /** 用于传递模型颜色信息.  */
    private var mColorHandle: Int = 0

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private val mViewMatrix = FloatArray(16)

    //存放矩阵模型，该矩阵用于将模型从对象空间（可以认为每个模型开始都位于宇宙的中心）移动到世界空间
    private val mModelMatrix = FloatArray(16)

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        //每10S完成一次旋转
        var time = SystemClock.uptimeMillis() % 10_000L
        var angleDegrees = (360.0f / 10_000f) * (time.toInt())

        //画三角形
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.rotateM(mModelMatrix, 0, angleDegrees, 0f, 0f, 1f)
        drawTriangle(mTriangle1Vertices)

        // Draw one translated a bit down and rotated to be flat on the ground.
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.translateM(mModelMatrix, 0, 0.0f, -1.0f, 0.0f)
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 1.0f, 0.0f, 0.0f)
        Matrix.rotateM(mModelMatrix, 0, angleDegrees, 0.0f, 0.0f, 1.0f)
        drawTriangle(mTriangle2Vertices)

        // Draw one translated a bit to the right and rotated to be facing to the left.
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.translateM(mModelMatrix, 0, 1.0f, 0.0f, 0.0f)
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 0.0f, 1.0f, 0.0f)
        Matrix.rotateM(mModelMatrix, 0, angleDegrees, 0.0f, 0.0f, 1.0f)
        drawTriangle(mTriangle3Vertices)
    }

    //为最终的组合矩阵分配存储空间，这将用来传入着色器程序
    private var mMVPMatrix = FloatArray(16)
    //每个顶点有多少字节组成，每次需要迈过这么一大步(每个顶点有7个元素，3个表示位置，4个表示颜色，7*4=28个字节)
    private val mStrideBytes = 7 * mBytesPerFloat
    //位置数据偏移量
    private val mPositionOffset = 0
    //一个元素的位置数据大小
    private val mPositionDataSize = 3

    //颜色数据偏移量
    private val mColorOffset = 3
    //一个元素的颜色数据大小
    private val mColorDataSize = 4


    /**
     * 从给定的顶点数据中绘制一个三角形
     * @param aTriangleBuffer 包含顶点数据的缓冲区
     */
    private fun drawTriangle(aTriangleBuffer: FloatBuffer) {
        aTriangleBuffer.position(mPositionOffset)

        GLES20.glVertexAttribPointer(
            mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT,
            false, mStrideBytes, aTriangleBuffer
        )

        GLES20.glEnableVertexAttribArray(mPositionHandle)

        //传入颜色信息
        aTriangleBuffer.position(mColorOffset)
        GLES20.glVertexAttribPointer(
            mColorHandle, mColorDataSize, GLES20.GL_FLOAT,
            false, mStrideBytes, aTriangleBuffer
        )

        GLES20.glEnableVertexAttribArray(mColorHandle)

        //将视图矩阵乘以模型矩阵，并将结果存放到MVP Matrix(model*view)
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0)

        //将上面计算好的视图模型矩阵乘以投影矩阵，并将结果存放到MVP Matrix(model * view * projection)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0)

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置OpenGl界面和当前视图相同的尺寸
        GLES20.glViewport(0, 0, width, height)

        //创建一个新的透视投影矩阵，高度保持不变，而高度根据横纵比而变化
        val ratio = width.toFloat() / height
        val left = -ratio
        val bottom = -1.0f
        val top = 1.0f
        val near = 1.0f
        val far = 10.0f

        Matrix.frustumM(mProjectionMatrix, 0, left, ratio, bottom, top, near, far)
    }

    override fun onSurfaceCreated(gl: GL10?, p1: EGLConfig?) {
        //设置GlSurfaceView为灰色背景
        GLES20.glClearColor(.5f, .5f, .5f, .5f)

        val eyeX = 0f
        val eyeY = 0f
        val eyeZ = 1.5f

        val lockX = 0f
        val lockY = 0f
        val lockZ = -5f

        val upX = 0f
        val upY = 1f
        val upZ = 0f

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lockX, lockY, lockZ, upX, upY, upZ)

        val vertexShader =
            ("uniform mat4 u_MVPMatrix;      \n"        // A constant representing the combined model/view/projection matrix.
                    + "attribute vec4 a_Position;     \n"        // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"        // Per-vertex color information we will pass in.
                    + "varying vec4 v_Color;          \n"        // This will be passed into the fragment shader.

                    + "void main()                    \n"        // The entry point for our vertex shader.

                    + "{                              \n"
                    + "   v_Color = a_Color;          \n"        // Pass the color through to the fragment shader.

                    // It will be interpolated across the triangle.
                    + "   gl_Position = u_MVPMatrix   \n"    // gl_Position is a special variable used to store the final position.

                    + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in

                    + "}                              \n")    // normalized screen coordinates.

        val fragmentShader =
            ("precision mediump float;       \n"        // Set the default precision to medium. We don't need as high of a

                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"        // This is the color from the vertex shader interpolated across the

                    // triangle per fragment.
                    + "void main()                    \n"        // The entry point for our fragment shader.

                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"        // Pass the color directly through the pipeline.

                    + "}                              \n")


        //加载顶点着色器
        var vertexShaderHandle: Int = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)

        if (vertexShaderHandle > 0) {
            //传入顶点着色器源代码
            GLES20.glShaderSource(vertexShaderHandle, vertexShader)
            //编译片段着色器
            GLES20.glCompileShader(vertexShaderHandle)

            //获取编译状态
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

            //如果编译失败，则删除着色器
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle)
                vertexShaderHandle = 0
            }
        }

        if (vertexShaderHandle == 0) {
            throw RuntimeException("Error creating vertex shader")
        }

        //加载片段着色器
        var fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)

        if (fragmentShaderHandle != 0) {
            // 传入片段着色器源代码
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader)

            // 编译片段着色器
            GLES20.glCompileShader(fragmentShaderHandle)

            // 获取编译状态
            val compileStatus = IntArray(1)
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

            // 如果编译失败，则删除着色器
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle)
                fragmentShaderHandle = 0
            }
        }

        if (fragmentShaderHandle == 0) {
            throw RuntimeException("Error creating fragment shader.")
        }

        //创建一个程序对象并将引用放进去
        var programHandle = GLES20.glCreateProgram()
        if (programHandle != 0) {
            //绑定顶点着色器到程序对象中
            GLES20.glAttachShader(programHandle, vertexShaderHandle)
            //绑定片段着色器到程序对象中
            GLES20.glAttachShader(programHandle, fragmentShaderHandle)
            //绑定属性
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position")
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color")
            //将两个着色器连接到程序
            GLES20.glLinkProgram(programHandle)

            //获取连接状态
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle)
                programHandle = 0
            }
        }

        if (programHandle == 0) {
            throw RuntimeException("Error creating program")
        }

        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix")
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position")
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color")

        //告诉OpenGL渲染的时候使用这个程序
        GLES20.glUseProgram(programHandle)


    }

    //存放投影矩阵，用于将场景投影到2D视角
    private var mProjectionMatrix = FloatArray(16)

    init {
        /**
         * Initialize the model data.
         */

        // Define points for equilateral triangles.

        // This triangle is red, green, and blue.
        val triangle1VerticesData = floatArrayOf(
            // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f
        )
        val triangle2VerticesData = floatArrayOf(
            // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.0f, 1.0f, 1.0f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            1.0f, 0.0f, 1.0f, 1.0f
        )
        val triangle3VerticesData = floatArrayOf(
            // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.size * mBytesPerFloat)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTriangle2Vertices = ByteBuffer.allocateDirect(triangle2VerticesData.size * mBytesPerFloat)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.size * mBytesPerFloat)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTriangle1Vertices.put(triangle1VerticesData).position(0)
        mTriangle2Vertices.put(triangle2VerticesData).position(0)
        mTriangle3Vertices.put(triangle3VerticesData).position(0)
    }


}