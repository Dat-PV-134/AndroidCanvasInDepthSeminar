package com.xgame.androidcanvasindepthseminar.chooser_game

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.graphics.toColorInt

class ChooserGameView : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var centerX = 0f
    private var centerY = 0f
    private val circles = mutableListOf<Circle>()
    private val circleCount = 5

    private data class Circle(
        var radius: Float = 0f,
        var alpha: Int = 255,
        var speed: Float = 1f,
        var currentScale: Float = 1f
    )

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        post {
            centerX = width / 2f
            centerY = height / 2f
            setupCircles()
            startAnimation()
        }
    }

    private fun setupCircles() {
        // Tạo các vòng tròn với tốc độ và kích thước khác nhau
        repeat(circleCount) { index ->
            circles.add(Circle(
                radius = width * (0.06f + index * 0.02f),  // Bán kính tăng dần
                alpha = 255 - (index * 40),                 // Độ trong suốt giảm dần
                speed = 1f + (index * 0.3f)                // Tốc độ tăng dần
            ))
        }
    }

    private fun startAnimation() {
        circles.forEachIndexed { index, circle ->
            ValueAnimator.ofFloat(0.8f, 1.3f).apply {
                duration = (1500 / circle.speed).toLong()  // Thời gian giảm khi speed tăng
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
                
                addUpdateListener { animator ->
                    circle.currentScale = animator.animatedValue as Float
                    invalidate()
                }
                
                start()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        setupCircles()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Vẽ từ ngoài vào trong để vòng trong đè lên vòng ngoài
        for (i in circles.size - 1 downTo 0) {
            val circle = circles[i]
            paint.apply {
                style = Paint.Style.FILL
                color = "#4CAF50".toColorInt()
                alpha = circle.alpha
            }
            
            // Vẽ vòng tròn với scale hiện tại
            canvas.drawCircle(
                centerX,
                centerY,
                circle.radius * circle.currentScale,
                paint
            )
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }
}