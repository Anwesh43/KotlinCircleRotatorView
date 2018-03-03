package ui.anwesome.com.circlerotatorlistview

/**
 * Created by anweshmishra on 03/03/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
import java.util.concurrent.ConcurrentLinkedQueue

class CircleRotatorListView(ctx : Context) : View(ctx) {
    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas : Canvas) {

    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class State(var prevScale : Float = 0f, var dir : Float = 0f, var scale : Float = 0f) {
        fun update(stopcb : (Float) -> Unit) {
            scale += dir * 0.1f
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if(dir == 0f) {
                dir = 1f - 2 * scale
                startcb()
            }
        }
    }
    data class ContainerState(var n : Int, var j : Int = 0, var jDir : Int = 1) {
        fun incrementCounter() {
            j += jDir
            if(j == n || j == -1) {
                jDir *= -1
                j += jDir
            }
        }
    }
    data class CircleRotator(var i : Int, var size : Float) {
        val state =  State()
        fun draw(canvas : Canvas, paint : Paint) {
            val x = size / 2 + i * size
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = size/15
            canvas.save()
            canvas.translate(x, size/2)
            canvas.rotate(180 * (1 - state.scale))
            canvas.drawCircle(0f, 0f, size/2, paint)
            canvas.restore()
        }
        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class CircleRotatorList(var n : Int, var w : Float, var h : Float) {
        val state = ContainerState(n)
        val rotators: ConcurrentLinkedQueue<CircleRotator> = ConcurrentLinkedQueue()
        init {
            if(n > 0) {
                val size = w / (2 * n)
                for(i in 0..n-1) {
                    rotators.add(CircleRotator(i, size))
                }
            }
        }
        fun draw(canvas : Canvas, paint : Paint) {
            for(i in 0..state.j) {
                rotators.at(i)?.draw(canvas, paint)
            }
        }
        fun update(stopcb : (Float) -> Unit) {
            rotators.at(state.j)?.update {
                state.incrementCounter()
                stopcb(it)
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            rotators?.at(state.j)?.startUpdating(startcb)
        }
    }
}
fun ConcurrentLinkedQueue<CircleRotatorListView.CircleRotator>.at(index : Int) : CircleRotatorListView.CircleRotator? {
    var i = 0
    forEach {
        if (i == index) {
            return it
        }
        i++
    }
    return null
}