package ui.anwesome.com.kotlincirclerotatorlistview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import ui.anwesome.com.circlerotatorlistview.CircleRotatorListView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = CircleRotatorListView.create(this)
        view.addOnRotateListener {
            Toast.makeText(this, "rotated $it", Toast.LENGTH_SHORT).show()
        }
        fullScreen()
    }
}
fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}
