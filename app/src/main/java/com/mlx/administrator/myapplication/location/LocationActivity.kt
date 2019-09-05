package com.mlx.administrator.myapplication.location

import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.os.postDelayed

class LocationActivity : AppCompatActivity() {

    var mLocation = ""
    var mLocationMgr: LocationManager? = null
    var mCriteria: Criteria= Criteria()
    var mHandler = Handler()
    var isLocationEnable = false


    private fun initLocation() {
        mLocationMgr = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        mCriteria.accuracy = Criteria.ACCURACY_HIGH
        mCriteria.isAltitudeRequired = true
        mCriteria.isBearingRequired = true
        mCriteria.powerRequirement =Criteria.POWER_LOW

        var bestProvider=mLocationMgr?.getBestProvider(mCriteria,true)
        if (mLocationMgr?.isProviderEnabled(bestProvider)!!){
            mLocation="定位类型=$bestProvider"
            beginLocation(bestProvider)
            isLocationEnable=true
        }else{
            isLocationEnable=false
        }

    }

    private fun beginLocation(method:String?) {
        mLocationMgr.requestLocationUpdates(method,300,0,mLocationListener)
        var location=mLocationMgr?.getLastKnownLocation(method)
//        TODO("已经拿到地理位置了")
    }

    var mLocationListener= object : LocationListener {
        override fun onLocationChanged(p0: Location?) {
            //        TODO("已经拿到地理位置了")
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {
        }
    }

    var mRefresh:Runnable?=null

    init {
        mRefresh= Runnable {
            if (!isLocationEnable) {
                initLocation()
                mHandler.postDelayed(mRefresh,1000)
            }
        }
    }

    override fun onDestroy() {
        mLocationMgr?.removeUpdates(mLocationListener)

        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()
        mHandler.removeCallbacks(mRefresh)
        initLocation()
        mHandler.postDelayed(mRefresh,1000)
    }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}