package com.mlx.administrator.myapplication.bluetooth

class BlueDevice (var name:String,var address:String,var state:Int){


    override fun toString(): String {
        val string="当前蓝牙名字:${name}\n\tMac地址:$address\n\t当前状态:$state"
        return string
    }
}