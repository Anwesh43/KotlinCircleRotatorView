package ui.anwesome.com.kotlincirclerotatorlistview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.circlerotatorlistview.CircleRotatorListView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CircleRotatorListView.create(this)
    }
}
