// kelas kustom yang meng-extend AppCompatEditText untuk memberikan fitur tambahan pada input teks password.
// mengatur visibility icon dan validasi password

package com.example.androidmkp.customView

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.example.androidmkp.R

class PasswordEditText : AppCompatEditText {
    private var isPasswordVisible = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        updateVisibilityIcon()

        val iconPadding = 24 * resources.displayMetrics.density.toInt()

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = compoundDrawablesRelative[2]
                if (drawableEnd != null) {
                    val touchAreaStart = right - drawableEnd.bounds.width() - iconPadding
                    val touchAreaEnd = right

                    if (event.rawX >= touchAreaStart && event.rawX <= touchAreaEnd) {
                        togglePasswordVisibility()
                        return@setOnTouchListener true
                    }
                }
            }
            return@setOnTouchListener false
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod = if (isPasswordVisible) null else PasswordTransformationMethod.getInstance()
        updateVisibilityIcon()
        invalidate()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateVisibilityIcon() {
        val icon = if (isPasswordVisible)
            R.drawable.baseline_visibility_24
        else
            R.drawable.baseline_visibility_off_24

        val drawable = context.getDrawable(icon)
        drawable?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            setCompoundDrawablesRelative(null, null, it, null)
        }
        requestLayout()
    }

    private fun validatePassword(password: String) {
        error = if (password.isNotEmpty() && password.length < 8) {
            context.getString(R.string.invalid_password)
        } else {
            null
        }
    }
}

