package ui.anwesome.com.circlerotatorlistview

/**
 * Created by anweshmishra on 03/03/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
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
}