package com.submission.app.story.shared.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.submission.app.story.R

class CustomButton : AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if(isEnabled) enabledBackground else disabledBackground
        gravity = Gravity.CENTER
        setTextColor(ContextCompat.getColor(context, R.color.white))
    }

    private fun init() {
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disabled) as Drawable
    }
}