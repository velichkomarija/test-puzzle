package com.velichkomarija.testpuzzle

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent


class CustomImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && foreground == null) {
            setColorFilter(0x50000000, PorterDuff.Mode.DARKEN)
            foreground = context.getDrawable(R.drawable.ic_check_black_24dp)
            foregroundGravity = Gravity.CENTER
        } else {
            colorFilter = null
            foreground = null
        }


        return super.onTouchEvent(event)
    }

}