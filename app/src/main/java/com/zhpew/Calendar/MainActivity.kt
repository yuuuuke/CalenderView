package com.zhpew.Calendar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.zhpew.Calendar.widget.CalenderView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalenderView(DataBean(Day(2023, 7, 17)), {}, {})
        }
    }
}