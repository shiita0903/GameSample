package jp.shiita.gamesample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.abs
import kotlin.random.Random

class AmidaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint()
    private val pinBitmap =
        ResourcesCompat.getDrawable(resources, R.drawable.ic_pin_red, null)!!.getBitmap()
    private val lineLists: List<MutableList<LineInfo>> =
        List(VERTICAL_LINE_COUNT) { mutableListOf<LineInfo>() }
    private lateinit var points: List<Pair<Float, Float>>
    private lateinit var traceCumulativeSum: List<Float>

    var progress: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        val lineCounts = (0 until VERTICAL_LINE_COUNT - 1).map { Random.nextInt(3, 5) }
            .scanL(0, Int::plus)

        val ys = (0 until lineCounts.last()).map { Random.nextFloat() }
            .sorted()
            .mapIndexed { i, v -> (v + i.toFloat() / (2 * lineCounts.last())) / 1.5f }  // 横線間の隙間をある程度保証
            .shuffled()

        (0 until VERTICAL_LINE_COUNT - 1).forEach { i ->
            (lineCounts[i] until lineCounts[i + 1]).map { ys[it] }
                .forEach { y ->
                    lineLists[i].add(LineInfo(y, true))
                    lineLists[i + 1].add(LineInfo(y, false))
                }
        }
        lineLists.forEach { it.sortBy(LineInfo::y) }
    }

    override fun onDraw(canvas: Canvas) {
        val w = width
        val h = height - VERTICAL_LINE_MARGIN

        drawVerticalLine(canvas, w)
        drawHorizontalLine(canvas, w, h)
        drawIntersection(canvas, w, h)
        drawPin(canvas)
    }

    fun calcPoints(lineNumber: Int): Float {
        val w = width
        val h = height - VERTICAL_LINE_MARGIN
        var n = lineNumber
        var yr = 0f

        points = mutableListOf<Pair<Float, Float>>().apply {
            add(calcX(w, n) to 0f)  // marginを考慮しない

            while (yr < lineLists[n].last().y) {
                val info = lineLists[n].map(LineInfo::y)
                    .binarySearch(yr)
                    .let {
                        val i = if (it < 0) it.inv() else it    // binarySearchをlowerBoundとして利用
                        lineLists[n][i]
                    }

                val y = calcY(h, info.y)
                add(calcX(w, n) to y)

                if (info.toRight) n++ else n--  // 右か左にずらす
                add(calcX(w, n) to y)

                yr = info.y + 1e-5f     // 次のlowerBoundを求めるために、微小な数を追加する
            }

            add(calcX(w, n) to height.toFloat())    // marginを考慮しない
        }

        traceCumulativeSum = points.zipWithNext { p0, p1 ->
            abs(p0.first - p1.first) + abs(p0.second - p1.second)
        }.scanL(0f, Float::plus)

        return traceCumulativeSum.last()
    }

    private fun drawVerticalLine(canvas: Canvas, w: Int) {
        paint.strokeWidth = VERTICAL_LINE_WIDTH.toFloat()
        paint.color = Color.BLACK

        (0 until VERTICAL_LINE_COUNT).forEach {
            val x = calcX(w, it)
            canvas.drawLine(x, 0f, x, height.toFloat(), paint)
        }
    }

    private fun drawHorizontalLine(canvas: Canvas, w: Int, h: Int) {
        paint.strokeWidth = HORIZONTAL_LINE_WIDTH.toFloat()
        paint.color = Color.BLACK

        (0 until VERTICAL_LINE_COUNT - 1).forEach { i ->
            val x0 = calcX(w, i)
            val x1 = calcX(w, i + 1)

            lineLists[i].filter { it.toRight }
                .map { h * it.y + VERTICAL_LINE_MARGIN / 2 }
                .forEach { y -> canvas.drawLine(x0, y, x1, y, paint) }
        }
    }

    private fun drawIntersection(canvas: Canvas, w: Int, h: Int) {
        paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)

        (0 until VERTICAL_LINE_COUNT).forEach { i ->
            val x = calcX(w, i)
            lineLists[i].map { calcY(h, it.y) }
                .forEach { y ->
                    canvas.drawCircle(x, y, INTERSECTION_RADIUS.toFloat(), paint)
                }
        }
    }

    private fun drawPin(canvas: Canvas) {
        if (progress == 0f) return

        val (x, y) = getPinPosition()
        canvas.drawBitmap(
            pinBitmap,
            x - pinBitmap.width / 2,
            y - pinBitmap.height,
            paint
        )
    }

    private fun calcX(w: Int, index: Int): Float =
        w.toFloat() * index / (VERTICAL_LINE_COUNT - 1)

    private fun calcY(h: Int, yr: Float): Float =
        h * yr + VERTICAL_LINE_MARGIN / 2

    private fun getPinPosition(): Pair<Float, Float> {
        val l = traceCumulativeSum.last() * progress
        val i = traceCumulativeSum.binarySearch(l)
        val index = if (i < 0) i.inv() - 1 else i - 1

        val restL = l - traceCumulativeSum[index]
        val (p0, p1) = points[index] to points[index + 1]
        Log.d("getPinPosition", "index = $i, l = $l, $p0, $p1, $restL")

        return if (p0.first == p1.first) {
            // 縦線
            p0.first to p0.second + restL
        } else {
            // 横線
            val x = if (p0.first < p1.first) p0.first + restL else p0.first - restL
            x to p0.second
        }
    }

    data class LineInfo(val y: Float, val toRight: Boolean)

    companion object {
        private const val HORIZONTAL_LINE_WIDTH = 8
        private const val VERTICAL_LINE_WIDTH = 12
        private const val VERTICAL_LINE_MARGIN = 64
        private const val VERTICAL_LINE_COUNT = 5
        private const val INTERSECTION_RADIUS = 12
    }
}