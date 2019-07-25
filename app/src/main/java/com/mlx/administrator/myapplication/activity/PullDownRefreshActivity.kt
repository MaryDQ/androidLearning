package com.mlx.administrator.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mlx.administrator.myapplication.R
import kotlinx.android.synthetic.main.activity_test.*

class PullDownRefreshActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initData()
    }

    private fun initData() {
        btnConfirm.setOnClickListener {
            if (etType.text.toString().isNotEmpty()) {
                mpvPic.setAnimType(etType.text.toString().toInt())
            }
        }

    }
}
