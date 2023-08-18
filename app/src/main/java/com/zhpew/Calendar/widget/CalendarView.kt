package com.zhpew.Calendar.widget

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

val RANGE = 3

@OptIn(ExperimentalPagerApi::class)
@Composable
public fun CalenderView(
    initData: DataBean,
    onMouthChange: (day: Day) -> Unit,
    onSelectedDayChange: (day: Day) -> Unit
) {

    val initIndex = initData.initDay.mouth + RANGE * 12
    val pageCount = 12 + RANGE * 12 * 2
    val selectedItem = remember {
        mutableStateOf(initData.initDay)
    }
    val toIndex = remember {
        mutableStateOf(0)
    }

    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        TitleView()
        val pagerState = rememberPagerState(pageCount = pageCount, initialPage = initIndex)
        LaunchedEffect(pagerState.currentPage) {
            val diffYear = (initData.initDay.mouth + pagerState.currentPage - initIndex) / 12
            val diffMouth = (initData.initDay.mouth + pagerState.currentPage - initIndex) % 12
            val thisDate =
                initData.initDay.copy(
                    year = initData.initDay.year + diffYear,
                    mouth = diffMouth,
                    day = 1
                )
            onMouthChange(thisDate)
            if (selectedItem.value.year != thisDate.year || selectedItem.value.mouth != thisDate.mouth) {
                selectedItem.value = thisDate
            }
        }

        LaunchedEffect(selectedItem.value) {
            onSelectedDayChange(selectedItem.value)
        }

        LaunchedEffect(toIndex.value) {
            if (toIndex.value + initIndex>= 0 && toIndex.value < pagerState.pageCount) {
                pagerState.animateScrollToPage(page = toIndex.value + initIndex)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            DataView(initData.initDay, it - initIndex, selectedItem, toIndex)
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
private fun DataView(
    day: Day,
    index: Int,
    selectedItem: MutableState<Day>,
    toIndex: MutableState<Int>,
) {

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
                data.add(Day(thisDate.year, thisDate.mouth + 1, i))
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
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
                                .padding(5.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(
                                    color = if (data[it * 7 + i] == selectedItem.value) colorResource(
                                        id = R.color.teal_700
                                    ) else colorResource(
                                        id = R.color.purple_200
                                    )
                                )
                                .clickable(interactionSource = MutableInteractionSource(), null) {
                                    if (data[it * 7 + i].year != thisDate.year || data[it * 7 + i].mouth != thisDate.mouth) {
                                        // not this mouth ,need scroll
                                        if (data[it * 7 + i].day > 15) {
                                            // to last mouth
                                            toIndex.value = index - 1
                                        } else {
                                            // to next mouth
                                            toIndex.value = index + 1
                                        }
                                    }
                                    selectedItem.value = data[it * 7 + i]
                                },
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
