package com.zhpew.Calendar.widget

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.zhpew.Calendar.DataBean
import com.zhpew.Calendar.Day
import com.zhpew.Calendar.R
import com.zhpew.Calendar.getMouthDay
import java.util.Calendar

val WEEK = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

// 年份范围，本年+前range年+后range年
val RANGE = 3


@OptIn(ExperimentalPagerApi::class)
@Composable
public fun CalenderView(initData: DataBean) {

    val initIndex = initData.initDay.mouth + RANGE * 12
    val pageCount = 12 + RANGE * 12 * 2
    val selectedItem = remember {
        mutableStateOf(initData.initDay)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TitleView()
        val pagerState = rememberPagerState(pageCount = pageCount, initialPage = initIndex)

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
        ) {
            DataView(initData.initDay, it - initIndex)
        }
    }
}

@Composable
private fun TitleView() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        for (index in WEEK.indices) {
            GravityText(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp), textStyle = TextStyle(
                    color = colorResource(
                        id = R.color.teal_200
                    )
                ), content = WEEK[index],
                vertical = Arrangement.Center,
                horizontal = Alignment.CenterHorizontally
            )
        }
    }
}

@Composable
private fun DataView(day: Day, index: Int) {
    // get this mouth
    val diffYear = (day.mouth + index) / 12
    val diffMouth = (day.mouth + index) % 12
    val thisDate = day.copy(year = day.year + diffYear, mouth = diffMouth)

    val calendar = Calendar.getInstance()
    calendar.set(thisDate.year, thisDate.mouth, 1)

    // get week of day
    val week = calendar.get(Calendar.DAY_OF_WEEK)

    val data = ArrayList<Day>()

    // last mouth date
    if (week != 1) {
        if (thisDate.mouth - 1 > 0) {
            val lasCalendar = Calendar.getInstance()
            lasCalendar.set(thisDate.year, thisDate.mouth - 1, 1)
            val lastMouth = lasCalendar.get(Calendar.MONTH)
            for (i in week - 2 downTo 0) {
                data.add(Day(thisDate.year, lastMouth, getMouthDay(thisDate.year, lastMouth) - i))
            }
        } else {
            // last year
            for (i in week - 2 downTo 0) {
                data.add(Day(thisDate.year - 1, 11, 31 - i))
            }
        }
    }

    // this mouth date
    for (i in 1..getMouthDay(thisDate.year, thisDate.mouth)) {
        data.add(Day(thisDate.year, thisDate.mouth, i))
    }

    //next mouth date
    if (data.size % 7 != 0) {
        val diffDay = 7 - data.size % 7
        if (thisDate.mouth + 1 > 11) {
            // next year
            for (i in 1..diffDay) {
                data.add(Day(thisDate.year + 1, 0, i))
            }
        } else {
            for (i in 1..diffDay) {
                data.add(Day(thisDate.year + 1, thisDate.mouth + 1, i))
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        content = {
            items(data.size / 7) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                ) {
                    for (i in 0 until 7) {
                        GravityText(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = colorResource(
                                        id = R.color.teal_700
                                    )
                                ),
                            content = "${data[it * 7 + i].day}",
                            textStyle = TextStyle(
                                color = if (data[it * 7 + i].mouth == thisDate.mouth)
                                    colorResource(id = R.color.black)
                                else
                                    colorResource(id = R.color.black_45),
                                fontSize = 13.sp,
                            ),
                            vertical = Arrangement.Center,
                            horizontal = Alignment.CenterHorizontally
                        )
                    }
                }
            }
        })
}

/**
 * 三种样式，本月，上个/下个月，选中
 */
@Composable
private fun Date(state: Int) {

}
