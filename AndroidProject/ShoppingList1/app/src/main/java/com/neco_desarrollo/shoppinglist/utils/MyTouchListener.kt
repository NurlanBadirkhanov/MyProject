package com.neco_desarrollo.shoppinglist.utils

import android.view.MotionEvent
import android.view.View

class MyTouchListener : View.OnTouchListener {
    var xDelta = 0.0f
    var yDelta = 0.0f
    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
               xDelta = v.x - event.rawX
               yDelta = v.y - event.rawY
            }
            MotionEvent.ACTION_MOVE->{
               v.x = xDelta +  event.rawX
               v.y = yDelta +  event.rawY
            }
        }
        return true
    }
}