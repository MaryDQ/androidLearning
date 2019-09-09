package com.mlx.administrator.myapplication.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService

class BlueToothActivity:AppCompatActivity(){
    private val TAG="blebleble"
    private var mBlueTooth:BluetoothAdapter?=null

    private fun initBluetooth(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
            var bm:BluetoothManager?=getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            mBlueTooth=bm?.adapter
        }else{
            mBlueTooth= BluetoothAdapter.getDefaultAdapter()
        }
        if (null==mBlueTooth) {
            Log.e(TAG,"本机未找到蓝牙功能")
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initBluetooth()
        initData()
    }

    private val mOpenCode=1

    private fun initData() {
        val intent=Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(intent,mOpenCode)
    }

    private fun beginDiscovery(){
        if (!mBlueTooth!!.isDiscovering){
            mBlueTooth?.startDiscovery()
        }
    }

    override fun onStart() {
        super.onStart()

    }

    private var discoveryReceiver:BroadcastReceiver?= object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var action=p1?.action
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                var device=p1?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//                TODO("refreshDevice")
            }else{
                //doNothing
            }
        }
    }
}