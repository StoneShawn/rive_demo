package com.example.rive_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.startup.AppInitializer
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.RiveArtboardRenderer
import app.rive.runtime.kotlin.RiveInitializer
import app.rive.runtime.kotlin.core.PlayableInstance
import app.rive.runtime.kotlin.core.Rive
import java.util.*

class MainActivity : AppCompatActivity() {


    private var keepGoing = true

    fun setTime() {
        val hours =
            (Calendar.getInstance().get(Calendar.HOUR) % 12f + Calendar.getInstance()
                .get(Calendar.MINUTE) / 60f + Calendar.getInstance()
                .get(Calendar.SECOND) / 3600f)
        clockView.setNumberState("Time", "isTime", hours)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTime()
        val h = Handler(Looper.getMainLooper())
        h.postDelayed(object : Runnable {
            override fun run() {
                // do stuff then
                // can call h again after work!
                if (keepGoing) {
                    setTime()
                    h.postDelayed(this, 360)
                }
            }
        }, 360) // 1 second del


        var starCount = 0
        //星星 輸入數字讓動畫跑到那狀態
        button.setOnClickListener {
            ratingView.setNumberState(
                "State Machine 1", "rating",
                (editText.text).toString().toFloat()
            )
        }

        ratingView.registerListener(object : RiveArtboardRenderer.Listener {
            override fun notifyLoop(animation: PlayableInstance) {

            }

            override fun notifyPause(animation: PlayableInstance) {

            }

            override fun notifyPlay(animation: PlayableInstance) {

            }

            //監聽view取得狀態
            override fun notifyStateChanged(stateMachineName: String, stateName: String) {
                Log.d("stateMachine", "${stateName}")
                starCount = when (stateName) {
                    "1_stars" -> 1
                    "2_stars" -> 2
                    "3_stars" -> 3
                    "4_stars" -> 4
                    "5_stars" -> 5
                    else -> 0
                }
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "有${starCount}個星星", Toast.LENGTH_SHORT).show()

                }


            }

            override fun notifyStop(animation: PlayableInstance) {

            }


        })
    }

    override fun onDetachedFromWindow() {
        // This the exit point for any RiveAnimationView, if we try to access
        // underlying properties (e.g. setNumberState() above) _after_ we detached, underlying
        // objects have probably been deallocated and this'll cause a crash.
        keepGoing = false
        super.onDetachedFromWindow()
    }

    private val clockView by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<RiveAnimationView>(R.id.clock)
    }

    private val ratingView by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<RiveAnimationView>(R.id.rating)
    }
    private val button by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<Button>(R.id.btn_go)
    }

    private val editText by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<EditText>(R.id.edit_go)
    }
}