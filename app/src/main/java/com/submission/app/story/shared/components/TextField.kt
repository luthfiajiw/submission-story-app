package com.submission.app.story.shared.components

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.submission.app.story.R

class TextField : AppCompatEditText, OnTouchListener {
    companion object {
        private const val TAG = "TextField"
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        attrs.apply {
            compoundDrawablePadding = 24
            textSize = 14f
            background = ContextCompat.getDrawable(context, R.drawable.textfield)
        }

        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val stringLength = s?.toString()?.length ?: 0

                error = if (stringLength < 6 && inputType == 129) {
                    "Password must be minimum 6 characters"
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return true
    }

}