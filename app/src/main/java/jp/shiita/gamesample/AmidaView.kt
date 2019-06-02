package jp.shiita.gamesample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class AmidaView : View {
    private val paint = Paint().apply { strokeWidth = LINE_WIDTH }

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun onDraw(canvas: Canvas) {
        val w = width - LINE_WIDTH
        val h = height - VERTICAL_LINE_MARGIN

        // 縦線
        (0 until VERTICAL_LINE_COUNT).forEach {
            val x = w * it / (VERTICAL_LINE_COUNT - 1) + LINE_WIDTH / 2
            canvas.drawLine(x, 0f, x, height.toFloat(), paint)
        }

        // 横線
        val lineCounts = (0 until VERTICAL_LINE_COUNT - 1).map { Random.nextInt(2, 5) }
            .scanL(0, Int::plus)
        val ys = (0 until lineCounts.last()).map { Random.nextFloat() }

        (0 until VERTICAL_LINE_COUNT - 1).forEach { i ->
            val x0 = w * i / (VERTICAL_LINE_COUNT - 1) + LINE_WIDTH / 2
            val x1 = w * (i + 1) / (VERTICAL_LINE_COUNT - 1) + LINE_WIDTH / 2

            (lineCounts[i] until lineCounts[i + 1]).map { h * ys[it] + VERTICAL_LINE_MARGIN / 2 }
                .forEach { y ->
                    canvas.drawLine(x0, y, x1, y, paint)
                }
        }
    }

    companion object {
        private const val LINE_WIDTH = 8f
        private const val VERTICAL_LINE_MARGIN = 64f
        private const val VERTICAL_LINE_COUNT = 5
    }
}