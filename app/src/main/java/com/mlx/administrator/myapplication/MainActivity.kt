package com.mlx.administrator.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mlx.administrator.myapplication.fragment.HomeFragment
import com.mlx.administrator.myapplication.fragment.PullRefreshLayoutFragment

class MainActivity : AppCompatActivity(), HomeFragment.OnFragmentInteractionListener,
    PullRefreshLayoutFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
