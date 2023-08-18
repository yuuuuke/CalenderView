package com.zhpew.Calendar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhpew.Calendar.widget.CalenderView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val day = Day(2023, 7, 17)
            val selectedItem = remember {
                mutableStateOf(day)
            }

            Column(modifier = Modifier.fillMaxSize()) {
                CalenderView(DataBean(day), {

                }, {
                    selectedItem.value = it
                })
                Text(
                    modifier = Modifier.padding(top = 30.dp),
                    text = "${selectedItem.value.year}年 ${selectedItem.value.mouth+1}月 ${selectedItem.value.day}日",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = colorResource(id = R.color.black)
                    )
                )
            }

        }
    }
}