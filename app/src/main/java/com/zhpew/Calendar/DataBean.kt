package com.zhpew.Calendar

data class DataBean(
    val initDay: Day,
//    val tagDay: ArrayList<Day>,
//    val dayData: HashMap<Day, String>
)


data class Day(val year: Int, val mouth: Int, val day: Int)