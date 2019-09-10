package com.mlx.administrator.myapplication.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.NfcA
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mlx.administrator.myapplication.R

class NfcActivity : AppCompatActivity() {

    val TAG = "NfcActivity001"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        initNfc()
    }

    var mNfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    var mFilters: Array<IntentFilter>? = null
    val techLists = arrayOf(arrayOf(NfcA::class.java.name), arrayOf(IsoDep::class.java.name))

    init {

    }

    private fun initNfc() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (null == mNfcAdapter) {
            Log.e(TAG, "当前手机不支持NFC")
        } else if (!mNfcAdapter!!.isEnabled) {
            Log.e(TAG, "请先在系统设置中启用NFC功能")
        }
        var intent = Intent(this, NfcActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        try {
            mFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED, "*/*"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //读标签之前先确定标签类型
    }

    override fun onResume() {
        super.onResume()
        if (null != mNfcAdapter && mNfcAdapter!!.isEnabled) {
            mNfcAdapter?.enableForegroundDispatch(this, pendingIntent, mFilters, techLists)
        }
    }

    override fun onPause() {
        super.onPause()
        if (null != mNfcAdapter && mNfcAdapter!!.isEnabled) {
            mNfcAdapter?.disableForegroundDispatch(this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        dealIntent(intent)
    }

    private fun dealIntent(intent: Intent?) {
        val action = intent?.action
        when (action) {
            NfcAdapter.ACTION_NDEF_DISCOVERED, NfcAdapter.ACTION_TECH_DISCOVERED, NfcAdapter.ACTION_TAG_DISCOVERED -> {
                val tag = intent?.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                //获取NFC卡片的序列号
                val ids = tag.id
                var card_info = "卡片的序列号为：${ByteArrayChange.ByteArrayToHexString(ids)}"
                val result = readGuardCard(tag)
                card_info = "$card_info\n详细信息如下:\n$result"
                Log.e(TAG, card_info)
            }
        }
    }


    fun readGuardCard(tag: Tag): String {
        var classic = MifareClassic.get(tag)
        var info = ""
        try {
            classic.connect()
            val type = classic.type
            var typeDesc = ""
            typeDesc = when (type) {
                MifareClassic.TYPE_CLASSIC -> {
                    "传统类型"
                }
                MifareClassic.TYPE_PLUS -> {
                    "增强类型"
                }
                MifareClassic.TYPE_PRO -> {
                    "专业类型"
                }
                else -> {
                    "传统类型"
                }
            }
            info = "\t卡片类型:${typeDesc}\n\t" +
                    "扇区数量:${classic.sectorCount}\n\t" +
                    "分块个数:${classic.blockCount}\n\t" +
                    "存储空间:${classic.size}字节"
        } catch (e: Exception) {
            e.printStackTrace()
            info = e.message.toString()
        } finally {
            try {
                classic.close()
            } catch (e: Exception) {
                e.printStackTrace()
                info = e.message.toString()
            }
        }
        return info
    }
}
