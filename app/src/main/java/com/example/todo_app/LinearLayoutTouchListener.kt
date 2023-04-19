package com.example.todo_app

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import java.lang.Math.abs


class LinearLayoutTouchListener(private val context: Context, private val needToDoTask: Need_to_do_task) : OnTouchListener {
    private var downX = 0f
    private var downY = 0f
    private var upX = 0f
    private var upY = 0f

    fun onRightToLeftSwipe() {
        Log.i(logTag, "RightToLeftSwipe!")
        showToast("RightToLeftSwipe")

        needToDoTask.swapToLeft();
    }

    fun onLeftToRightSwipe() {
        Log.i(logTag, "LeftToRightSwipe!")
        //showToast("LeftToRightSwipe")
        needToDoTask.swapToRight();
    }

    fun onTopToBottomSwipe() {
        Log.i(logTag, "onTopToBottomSwipe!")
        showToast("onTopToBottomSwipe")

    }

    fun onBottomToTopSwipe() {
        Log.i(logTag, "onBottomToTopSwipe!")
        showToast("onBottomToTopSwipe")
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                return true
            }

            MotionEvent.ACTION_UP -> {
                upX = event.x
                upY = event.y
                val deltaX = downX - upX
                val deltaY = downY - upY

                // swipe horizontal?
                if (abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        onLeftToRightSwipe()
                    } else if (deltaX > 0) {
                        onRightToLeftSwipe()
                    }
                    return true
                }

                // swipe vertical?
                if (abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {
                        onTopToBottomSwipe()
                    } else if (deltaY > 0) {
                        onBottomToTopSwipe()
                    }
                    return true
                }
            }
        }
        return false
    }

    companion object {
        const val logTag = "ActivitySwipeDetector"
        const val MIN_DISTANCE = 100
    }
}