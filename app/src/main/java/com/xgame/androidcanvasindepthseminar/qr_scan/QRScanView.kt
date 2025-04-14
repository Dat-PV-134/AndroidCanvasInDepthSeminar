package com.xgame.androidcanvasindepthseminar.qr_scan

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class QRScanView : View {
    private val myPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.GREEN
        }
    }

    private var scanLineY = 0f
    private var animator: ValueAnimator? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        post { startAnimation() }
    }

    private fun startAnimation() {
        animator?.cancel()
        
        animator = ValueAnimator.ofFloat(0f, height.toFloat() + width * 0.3f).apply {
            duration = 2000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            
            addUpdateListener { animation ->
                scanLineY = animation.animatedValue as Float
                invalidate()
            }
        }
        animator?.start()
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawScanAnim(canvas)
    }

    private fun drawScanAnim(canvas: Canvas) {
        val scanHeight = width * 0.3f
        myPaint.shader = LinearGradient(
            0f,
            scanLineY - scanHeight,
            0f,
            scanLineY,
            intArrayOf(
                Color.argb(0, 0, 255, 0),    // Trong suốt
                Color.argb(128, 0, 255, 0),  // Nửa trong suốt
                Color.argb(0, 0, 255, 0)     // Trong suốt
            ),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )

        canvas.drawRect(
            RectF(
                0f,
                scanLineY - scanHeight,
                width.toFloat(),
                scanLineY
            ),
            myPaint
        )
    }

    private fun drawBackground(canvas: Canvas) {
        // Đảm bảo xóa shader trước khi vẽ background
        myPaint.shader = null
        myPaint.color = Color.GREEN
        myPaint.strokeWidth = width * 0.02f

        // draw top left
        canvas.drawLine(
            0f,
            0f,
            0f,
            0f + width * 0.1f,
            myPaint
        )

        canvas.drawLine(
            0f,
            0f,
            0f + width * 0.1f,
            0f,
            myPaint
        )

        // draw top right
        canvas.drawLine(
            width.toFloat(),
            0f,
            width.toFloat(),
            0f + width * 0.1f,
            myPaint
        )

        canvas.drawLine(
            width.toFloat(),
            0f,
            width.toFloat() - width * 0.1f,
            0f,
            myPaint
        )

        // draw bottom left
        canvas.drawLine(
            0f,
            height.toFloat(),
            0f,
            height.toFloat() - width * 0.1f,
            myPaint
        )

        canvas.drawLine(
            0f,
            height.toFloat(),
            0f + width * 0.1f,
            height.toFloat(),
            myPaint
        )

        // draw bottom right
        canvas.drawLine(
            width.toFloat(),
            height.toFloat(),
            width.toFloat(),
            height.toFloat() - width * 0.1f,
            myPaint
        )

        canvas.drawLine(
            width.toFloat(),
            height.toFloat(),
            width.toFloat() - width * 0.1f,
            height.toFloat(),
            myPaint
        )
    }
}