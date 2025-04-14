package com.xgame.androidcanvasindepthseminar.auto_infinity_scroll

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.io.IOException
import java.io.InputStream

class InfinitySplashScrollView  : View {
    // Paint chính với ANTI_ALIAS để vẽ mượt và FILTER_BITMAP để làm mịn bitmap khi scale
    private val bitmapPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    }

    // Path để vẽ hình chữ nhật bo góc
    private val roundedRectPath = Path()
    // Độ bo tròn của góc, có thể điều chỉnh giá trị để tăng/giảm độ cong
    private val cornerRadius = 20f

    // Ba danh sách bitmap cho ba cột
    private val listBitmap1 by lazy { ArrayList<Bitmap?>() }  // Cột trái
    private val listBitmap2 by lazy { ArrayList<Bitmap?>() }  // Cột giữa
    private val listBitmap3 by lazy { ArrayList<Bitmap?>() }  // Cột phải

    // Các biến điều khiển animation
    private var scrollDownFactor = 0f  // Hệ số cuộn xuống (0->1) cho cột 1 và 3
    private var scrollUpFactor = 0f    // Hệ số cuộn lên (1->0) cho cột 2
    private var animator: ValueAnimator? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    
    init {
        context?.let {
            listBitmap1.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_1_item_1.webp"
                )
            )
            listBitmap1.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_1_item_2.webp"
                )
            )
            listBitmap1.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_1_item_3.webp"
                )
            )
            listBitmap1.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_1_item_4.webp"
                )
            )
            listBitmap1.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_1_item_5.webp"
                )
            )
            listBitmap1.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_1_item_6.webp"
                )
            )

            listBitmap2.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_2_item_1.webp"
                )
            )
            listBitmap2.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_2_item_2.webp"
                )
            )
            listBitmap2.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_2_item_3.webp"
                )
            )
            listBitmap2.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_2_item_4.webp"
                )
            )
            listBitmap2.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_2_item_5.webp"
                )
            )
            listBitmap2.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_2_item_6.webp"
                )
            )

            listBitmap3.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_3_item_1.webp"
                )
            )
            listBitmap3.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_3_item_2.webp"
                )
            )
            listBitmap3.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_3_item_3.webp"
                )
            )
            listBitmap3.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_3_item_4.webp"
                )
            )
            listBitmap3.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_3_item_5.webp"
                )
            )
            listBitmap3.add(
                getImageBitmap(
                    it,
                    "infinity_scroll_anim_images/list_3_item_6.webp"
                )
            )

            startAnimation()
        }
    }

    // Hàm bắt đầu animation
    private fun startAnimation() {
        animator?.cancel()  // Hủy animation cũ nếu có
        
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 14000  // Thời gian một chu kỳ animation (14 giây)
            repeatMode = ValueAnimator.RESTART  // Chạy lại từ đầu khi kết thúc
            repeatCount = ValueAnimator.INFINITE  // Lặp vô tận
            interpolator = LinearInterpolator()  // Tốc độ đều
            
            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                scrollUpFactor = 1f - value  // Ngược chiều cho cột giữa
                scrollDownFactor = value     // Chiều xuống cho cột 1 và 3
                invalidate()  // Yêu cầu vẽ lại view
            }
        }
        animator?.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawAnimView(canvas)
    }

    // Hàm vẽ bitmap với góc bo tròn
    private fun drawBitmapRounded(
        canvas: Canvas,
        bitmap: Bitmap,
        srcRect: Rect,    // Vùng cắt từ bitmap gốc (để center crop)
        dstRect: RectF,   // Vùng đích trên canvas (vị trí và kích thước hiển thị)
        paint: Paint
    ) {
        // Tạo shader từ bitmap để vẽ với mode CLAMP (giữ màu cạnh ngoài cùng)
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        
        // Tính toán ma trận scale để fit bitmap vào destination rect
        val shader_matrix = Matrix()
        // Tỷ lệ scale theo chiều rộng và cao
        val scale_x = dstRect.width() / srcRect.width()
        val scale_y = dstRect.height() / srcRect.height()
        shader_matrix.setScale(scale_x, scale_y)
        // Dịch chuyển shader đến vị trí cần vẽ
        shader_matrix.postTranslate(dstRect.left, dstRect.top)
        
        // Áp dụng ma trận vào shader và gán shader cho paint
        shader.setLocalMatrix(shader_matrix)
        paint.shader = shader

        // Tạo path hình chữ nhật bo góc
        roundedRectPath.reset()
        roundedRectPath.addRoundRect(dstRect, cornerRadius, cornerRadius, Path.Direction.CW)
        
        // Vẽ path với shader đã được cấu hình
        canvas.drawPath(roundedRectPath, paint)
        // Reset shader để không ảnh hưởng các lần vẽ sau
        paint.shader = null
    }

    // Hàm vẽ chính
    private fun drawAnimView(canvas: Canvas) {
        // Tính toán kích thước và khoảng cách
        val columnSpacing = width/30f  // Khoảng cách giữa các cột
        val columnWidth = (width - 4 * columnSpacing) / 3f  // Chiều rộng mỗi cột
        val itemWidth = columnWidth
        val itemHeight = itemWidth * 1.5f  // Tỷ lệ 2:3
        val singleListHeight = (itemHeight + columnSpacing) * listBitmap1.size

        var nextY1 = -singleListHeight
        while (nextY1 < height) {
            listBitmap1.forEach { bitmap ->
                bitmap?.let {
                    val srcRect = calculateCenterCropRect(it.width, it.height, 2, 3)
                    val currentY = nextY1 + scrollDownFactor * singleListHeight
                    val dstRect = RectF(
                        columnSpacing,
                        currentY,
                        columnSpacing + columnWidth,
                        currentY + itemHeight
                    )
                    drawBitmapRounded(canvas, it, srcRect, dstRect, bitmapPaint)
                    nextY1 += itemHeight + columnSpacing
                }
            }
        }

        var nextY2 = -singleListHeight
        while (nextY2 < height) {
            listBitmap2.forEach { bitmap ->
                bitmap?.let {
                    val srcRect = calculateCenterCropRect(it.width, it.height, 2, 3)
                    val currentY = nextY2 + scrollUpFactor * singleListHeight
                    val dstRect = RectF(
                        2 * columnSpacing + columnWidth,
                        currentY,
                        2 * columnSpacing + 2 * columnWidth,
                        currentY + itemHeight
                    )
                    drawBitmapRounded(canvas, it, srcRect, dstRect, bitmapPaint)
                    nextY2 += itemHeight + columnSpacing
                }
            }
        }

        var nextY3 = -singleListHeight
        while (nextY3 < height) {
            listBitmap3.forEach { bitmap ->
                bitmap?.let {
                    val srcRect = calculateCenterCropRect(it.width, it.height, 2, 3)
                    val currentY = nextY3 + scrollDownFactor * singleListHeight
                    val dstRect = RectF(
                        3 * columnSpacing + 2 * columnWidth,
                        currentY,
                        3 * columnSpacing + 3 * columnWidth,
                        currentY + itemHeight
                    )
                    drawBitmapRounded(canvas, it, srcRect, dstRect, bitmapPaint)
                    nextY3 += itemHeight + columnSpacing
                }
            }
        }
    }

    // Hàm tính toán vùng cắt để center crop bitmap
    private fun calculateCenterCropRect(bitmapWidth: Int, bitmapHeight: Int, targetRatioX: Int, targetRatioY: Int): Rect {
        val targetRatio = targetRatioX.toFloat() / targetRatioY
        val bitmapRatio = bitmapWidth.toFloat() / bitmapHeight

        return if (bitmapRatio > targetRatio) {
            // Ảnh quá rộng, cắt hai bên
            val newWidth = (bitmapHeight * targetRatio).toInt()
            val xOffset = (bitmapWidth - newWidth) / 2
            Rect(xOffset, 0, xOffset + newWidth, bitmapHeight)
        } else {
            // Ảnh quá cao, cắt trên dưới
            val newHeight = (bitmapWidth / targetRatio).toInt()
            val yOffset = (bitmapHeight - newHeight) / 2
            Rect(0, yOffset, bitmapWidth, yOffset + newHeight)
        }
    }
}

fun getImageBitmap(context: Context, filePath: String) : Bitmap? {
    val assetManager = context.assets
    val inputStream: InputStream
    var bitmap: Bitmap? = null
    try {
        inputStream = assetManager.open(filePath)
        bitmap = BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bitmap
}