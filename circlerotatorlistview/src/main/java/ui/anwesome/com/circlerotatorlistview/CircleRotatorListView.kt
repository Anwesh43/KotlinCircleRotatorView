package ui.anwesome.com.circlerotatorlistview

/**
 * Created by anweshmishra on 03/03/18.
 */
import android.app.Activity
import android.view.*
import android.content.*
import android.graphics.*
import android.util.Log
import java.util.concurrent.ConcurrentLinkedQueue

class CircleRotatorListView(ctx : Context, var n : Int = 5) : View(ctx) {
    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class State(var prevScale : Float = 0f, var dir : Float = 0f, var scale : Float = 0f) {
        fun update(stopcb : (Float) -> Unit) {
            scale += dir * 0.1f
            Log.d("updating","$scale")
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                Log.d("stopped","$prevScale")
                stopcb(scale)
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if(dir == 0f) {
                dir = 1f - 2 * scale
                Log.d("startUpdating","$dir")
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
            val x =  i * size
            canvas.save()
            canvas.translate(x, size/2)
            canvas.rotate(180 * (1 - (1 - 2 * (i % 2)) * state.scale))
            canvas.drawCircle(size/2, 0f, size/2, paint)
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
                val size = (w)*0.9f / (n)
                for(i in 0..n-1) {
                    rotators.add(CircleRotator(i, size))
                }
            }
        }
        fun draw(canvas : Canvas, paint : Paint) {
            canvas.save()
            canvas.translate(-Math.min(w, h) / 120, h / 2)
            for(i in 0..state.j) {
                rotators.at(i)?.draw(canvas, paint)
            }
            canvas.restore()
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
    data class Renderer(var view : CircleRotatorListView, var time : Int = 0) {
        var circleRotatorList : CircleRotatorList ?= null
        val animator = Animator(view)
        fun render(canvas : Canvas, paint : Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                circleRotatorList = CircleRotatorList(view.n, w, h)
                paint.color = Color.parseColor("#1565C0")
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = Math.min(w, h)/45
            }
            canvas.drawColor(Color.parseColor("#212121"))
            circleRotatorList?.draw(canvas, paint)
            time++
            animator.animate {
                circleRotatorList?.update {
                    animator.stop()
                }
            }

        }
        fun handleTap() {
            circleRotatorList?.startUpdating {
                animator.start()
            }
        }
    }
    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex : Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    companion object {
        fun create(activity : Activity):CircleRotatorListView {
            val view = CircleRotatorListView(activity)
            activity.setContentView(view)
            return view
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