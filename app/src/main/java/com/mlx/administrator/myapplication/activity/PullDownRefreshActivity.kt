package com.mlx.administrator.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mlx.administrator.myapplication.R

class PullDownRefreshActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_down_refresh)
        initData()
    }

    private fun initData() {


    }
}
