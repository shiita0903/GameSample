package jp.shiita.gamesample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class AmidaView : View {
    private val paint = Paint()
    val pointsList: List<MutableList<Float>> = List(VERTICAL_LINE_COUNT) { mutableListOf<Float>() }

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun onDraw(canvas: Canvas) {
        val w = width - VERTICAL_LINE_WIDTH
        val h = height - VERTICAL_LINE_MARGIN

        drawVerticalLine(canvas, w)
        drawHorizontalLineAndUpdatePoints(canvas, w, h)
        drawIntersection(canvas, w)
    }

    private fun drawVerticalLine(canvas: Canvas, w: Int) {
        paint.strokeWidth = VERTICAL_LINE_WIDTH.toFloat()
        paint.color = Color.BLACK

        (0 until VERTICAL_LINE_COUNT).forEach {
            val x = calcX(w, it)
            canvas.drawLine(x, 0f, x, height.toFloat(), paint)
        }
    }

    private fun drawHorizontalLineAndUpdatePoints(canvas: Canvas, w: Int, h: Int) {
        paint.strokeWidth = HORIZONTAL_LINE_WIDTH.toFloat()
        paint.color = Color.BLACK

        pointsList.forEach { it.clear() }
        val lineCounts = (0 until VERTICAL_LINE_COUNT - 1).map { Random.nextInt(3, 5) }
            .scanL(0, Int::plus)
        val ys = (0 until lineCounts.last()).map { Random.nextFloat() }
            .sorted()
            .mapIndexed { i, v -> (v + i.toFloat() / (2 * lineCounts.last())) / 1.5f }  // 横線間の隙間をある程度保証
            .shuffled()

        (0 until VERTICAL_LINE_COUNT - 1).forEach { i ->
            val x0 = calcX(w, i)
            val x1 = calcX(w, i + 1)

            (lineCounts[i] until lineCounts[i + 1]).map { h * ys[it] + VERTICAL_LINE_MARGIN / 2 }
                .forEach { y ->
                    canvas.drawLine(x0, y, x1, y, paint)
                    pointsList[i].add(y)
                    pointsList[i + 1].add(y)
                }
        }
        pointsList.forEach { it.sort() }
    }

    private fun drawIntersection(canvas: Canvas, w: Int) {
        paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)

        (0 until VERTICAL_LINE_COUNT).forEach { i ->
            val x = calcX(w, i)
            pointsList[i].forEach { y ->
                canvas.drawCircle(x, y, INTERSECTION_RADIUS.toFloat(), paint)
            }
        }
    }

    private fun calcX(w: Int, index: Int): Float =
        w.toFloat() * index / (VERTICAL_LINE_COUNT - 1) + VERTICAL_LINE_WIDTH / 2

    companion object {
        private const val HORIZONTAL_LINE_WIDTH = 8
        private const val VERTICAL_LINE_WIDTH = 12
        private const val VERTICAL_LINE_MARGIN = 64
        private const val VERTICAL_LINE_COUNT = 5
        private const val INTERSECTION_RADIUS = 12
    }
}