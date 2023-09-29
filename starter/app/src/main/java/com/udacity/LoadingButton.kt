package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var btnTextSize: Float = resources.getDimension(R.dimen.default_text_size)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = btnTextSize
    }

    private val valueAnimator = ValueAnimator()
    private var rectWidth:Float = 0.0f
    private var sweepAngle:Float = 0f

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {
        isClickable = true
        valueAnimator.setFloatValues(0f, 1f)
        valueAnimator.duration = 1000
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw base rectangle
        paint.color = resources.getColor(R.color.colorPrimary)
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        // Draw darker rectangle which is animated
        paint.color = resources.getColor(R.color.colorPrimaryDark)
        canvas.drawRect(0f, 0f, rectWidth, heightSize.toFloat(), paint)

        // Write text
        paint.color = Color.WHITE
        val textWidth = paint.measureText(context.getString(R.string.button_download))
        canvas.drawText(context.getString(R.string.button_download),
            widthSize.toFloat()/2, heightSize.toFloat()/2, paint)

        // Draw yellow circle which is animated
        canvas.save()
        canvas.translate(widthSize / 2 + textWidth / 2 + 20, heightSize / 2 - btnTextSize / 2)
        paint.color = resources.getColor(R.color.colorAccent)
        canvas.drawArc(RectF(0f, 0f, btnTextSize, btnTextSize), 0F, sweepAngle, true, paint)

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
        valueAnimator.start()
        valueAnimator.addUpdateListener { animator ->
            rectWidth = widthSize * animator.getAnimatedValue() as Float
            sweepAngle = 360 * animator.getAnimatedValue() as Float
            invalidate()
        }
        return true
    }
}