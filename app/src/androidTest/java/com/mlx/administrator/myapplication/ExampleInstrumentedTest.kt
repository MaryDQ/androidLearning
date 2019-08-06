package com.mlx.administrator.myapplication

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.mlx.administrator.myapplication", appContext.packageName)
    }

    class UpdateService(private val context: Context) : Service() {
        override fun onBind(intent: Intent?): IBinder {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}
