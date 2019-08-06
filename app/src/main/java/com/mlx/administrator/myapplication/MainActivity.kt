package com.mlx.administrator.myapplication

import android.opengl.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_test_matrix.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_matrix)
        initData()
    }

    private fun getMatrix(): android.graphics.Matrix {
        var text01 = et1.text.toString().toFloat()
        var text02 = et2.text.toString().toFloat()
        var text03 = et3.text.toString().toFloat()
        var text04 = et4.text.toString().toFloat()
        var text05 = et5.text.toString().toFloat()
        var text06 = et6.text.toString().toFloat()
        var text07 = et7.text.toString().toFloat()
        var text08 = et8.text.toString().toFloat()
        var text09 = et9.text.toString().toFloat()
        val matrix = android.graphics.Matrix()

        matrix.setValues(
            floatArrayOf(
                text01, text02, text03,
                text04, text05, text06,
                text07, text08, text09
            )
        )
        return matrix
    }

    private fun initData() {
        btnDoMatrix.setOnClickListener {
            mpv.setMartixFromOut(matrix = getMatrix())
        }
    }
}
