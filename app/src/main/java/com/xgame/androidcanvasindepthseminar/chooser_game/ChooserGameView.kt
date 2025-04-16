package com.xgame.androidcanvasindepthseminar.chooser_game

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.set

class ChooserGameView : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circles = mutableListOf<Circle>()
    private val circleCount = 5

    private val userTouchPoints by lazy { HashMap<Int, PointF?>() }

    private var timer: CountDownTimer? = null
    private var elapsedTime = 0L
    private var isTimerRunning = false

    private var winner: PointF? = null
    private var isWinningTime = false


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
            setupCircles()
            startAnimation()
        }
    }

    private fun setupCircles() {
        // Tạo các vòng tròn với tốc độ và kích thước khác nhau
        repeat(circleCount) { index ->
            circles.add(
                Circle(
                    radius = width * (0.06f + index * 0.02f),  // Bán kính tăng dần
                    alpha = 255 - (index * 40),                 // Độ trong suốt giảm dần
                    speed = 1f + (index * 0.3f)                // Tốc độ tăng dần
                )
            )
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
        setupCircles()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!isWinningTime) {
            userTouchPoints.forEach { touchPoint ->
                touchPoint.value?.let {
                    drawCircleAnim(canvas, it)
                }
            }
        } else {
            winner?.let {
                drawCircleAnim(canvas, it)
            }
        }
    }

    private fun drawCircleAnim(canvas: Canvas, point: PointF) {
        for (i in circles.size - 1 downTo 0) {
            val circle = circles[i]
            paint.apply {
                style = Paint.Style.FILL
                color = "#4CAF50".toColorInt()
                alpha = circle.alpha
            }

            point.let {
                // Vẽ vòng tròn với scale hiện tại
                canvas.drawCircle(
                    it.x,
                    it.y,
                    circle.radius * circle.currentScale,
                    paint
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isWinningTime) {
            event?.let { event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                        userTouchPoints.put(
                            event.getPointerId(event.actionIndex),
                            PointF(event.getX(event.actionIndex), event.getY(event.actionIndex))
                        )
                        userTouchPoints.forEach {
                            Log.e("DatPV", it.toString())
                        }

                        if (userTouchPoints.size >= 2) {
                            stopTimer()
                            startTimer()
                        }
                    }

                    MotionEvent.ACTION_MOVE -> {
                        for (i in 0 until event.pointerCount) {
                            val pointerId = event.getPointerId(i)
                            userTouchPoints[pointerId]?.let { point ->
                                point.x = event.getX(i)
                                point.y = event.getY(i)
                            } ?: run {
                                userTouchPoints[pointerId] = PointF(event.getX(i), event.getY(i))
                            }
                        }
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                        userTouchPoints.remove(event.getPointerId(event.actionIndex))

                        if (userTouchPoints.size < 2) {
                            stopTimer()
                        }
                    }
                }
            }
        }
        invalidate()
        return true
    }

    private fun startTimer() {
        isWinningTime = false
        isTimerRunning = true
        elapsedTime = 0L

        timer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                winner = userTouchPoints.values.random()
                isWinningTime = true
                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000)
                    userTouchPoints.clear()
                    isWinningTime = false
                    winner = null
                }
            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTimer()
    }
}