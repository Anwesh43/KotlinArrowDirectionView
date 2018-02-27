package ui.anwesome.com.kotlinarrowdirectionview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.arrowdirectionview.ArrowDirectionView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ArrowDirectionView.create(this)
    }
}
