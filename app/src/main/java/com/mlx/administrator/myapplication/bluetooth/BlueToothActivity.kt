package com.mlx.administrator.myapplication.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList

class BlueToothActivity : AppCompatActivity() {
    private val TAG = "blebleble"
    private var mBlueTooth: BluetoothAdapter? = null

    private fun initBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            var bm: BluetoothManager? = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            mBlueTooth = bm?.adapter
        } else {
            mBlueTooth = BluetoothAdapter.getDefaultAdapter()
        }
        if (null == mBlueTooth) {
            Log.e(TAG, "本机未找到蓝牙功能")
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initBluetooth()
        initData()
    }

    private val mOpenCode = 1

    private fun initData() {
        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(intent, mOpenCode)
    }

    private fun beginDiscovery() {
        if (!mBlueTooth!!.isDiscovering) {
            mBlueTooth?.startDiscovery()
        }
    }

    override fun onStart() {
        super.onStart()
        mHandler.postDelayed(mRefreshRunnable,50)

        val discoveryFilter = IntentFilter()
        discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND)
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        discoveryFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        // 注册蓝牙设备搜索的广播接收器
        registerReceiver(discoveryReceiver, discoveryFilter)

    }

    private var discoveryReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var action = p1?.action
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                var device = p1?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                refreshDevice(device, device?.bondState)
            } else {
                //doNothing
            }
        }
    }

    private var mDeviceList = ArrayList<BlueDevice>()

    private fun initBluetoothDevice() {
        mDeviceList.clear()

        var bondedDevice = mBlueTooth!!.bondedDevices

        for (item in bondedDevice) {
            mDeviceList.add(BlueDevice(name = item.name, address = item.address, state = item.bondState))
        }

        Log.e(TAG, Arrays.deepToString(mDeviceList.toArray()))

    }


    private var mHandler = Handler()

    private var mRefreshRunnable = object : Runnable {
        override fun run() {
            beginDiscovery()
            mHandler.postDelayed(this, 2000)
        }
    }

    private fun cancelDiscovery(){
        mHandler.removeCallbacks(mRefreshRunnable)
        if (mBlueTooth!!.isDiscovering){
            mBlueTooth!!.cancelDiscovery()
        }
    }

    override fun onStop() {
        super.onStop()
        cancelDiscovery()
        unregisterReceiver(discoveryReceiver)
    }

    private fun refreshDevice(device: BluetoothDevice?, state: Int?) {

    }
}