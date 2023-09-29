package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()
    private var animatedBtnWidth = 0f
    private var animatedSweepAngle = 0f

    private var btnColorDefault = 0
    private var btnColorInProgress = 0
    private var circleColor = 0
    private var textColor = 0
    private var btnTextSize = resources.getDimension(R.dimen.default_text_size)
    private var btnText = resources.getString(R.string.button_download)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = btnTextSize
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {

            }
            ButtonState.Loading -> {
                btnText = resources.getString(R.string.button_loading)
                startAnimation()

            }
            ButtonState.Completed -> {
                btnText = resources.getString(R.string.button_download)
                animatedBtnWidth = 0f
                animatedSweepAngle = 0f
                valueAnimator.cancel()
                invalidate()
            }
        }
    }

    private fun startAnimation() {
        valueAnimator.addUpdateListener { animator ->
            animatedBtnWidth = widthSize * animator.getAnimatedValue() as Float
            animatedSweepAngle = 360 * animator.getAnimatedValue() as Float
            invalidate()
        }
        valueAnimator.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                animatedBtnWidth = 0f
                animatedSweepAngle = 0f
                if (buttonState == ButtonState.Loading) {
                    buttonState = ButtonState.Loading
                }
            }
        })
        valueAnimator.start()
    }


    init {
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            btnColorDefault = getColor(R.styleable.LoadingButton_btnColorDefault, 0)
            btnColorInProgress = getColor(R.styleable.LoadingButton_btnColorInProgress, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
        }

        valueAnimator.setFloatValues(0f, 1f)
        valueAnimator.duration = 1000
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw base rectangle
        paint.color = btnColorDefault
        canvas.drawRoundRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), heightSize.toFloat()/2, heightSize.toFloat()/2, paint)

        // Draw darker animated rectangle
        paint.color = btnColorInProgress
        canvas.drawRoundRect(0f, 0f, animatedBtnWidth, heightSize.toFloat(), heightSize.toFloat()/2, heightSize.toFloat()/2, paint)

        // Write text
        paint.color = textColor
        val textWidth = paint.measureText(btnText)
        canvas.drawText(btnText,
            widthSize.toFloat() / 2,
            heightSize / 2 - (paint.descent() + paint.ascent()) / 2,
            paint)

        // Draw yellow animated circle
        canvas.save()
        canvas.translate((widthSize - heightSize).toFloat(), 0f)
        paint.color = resources.getColor(R.color.colorAccent)
        canvas.drawArc(RectF(0f, 0f, heightSize.toFloat(), heightSize.toFloat()),
            0F, animatedSweepAngle, true, paint)
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h

        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        return true
    }
}