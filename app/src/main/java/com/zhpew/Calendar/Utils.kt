package com.zhpew.Calendar

val BigMouth = arrayOf(0, 2, 4, 6, 7, 9, 11)
val Special = 1

// first is 1
fun getMouthDay(year:Int,mouth:Int):Int{
    return if(mouth in BigMouth){
        31
    }else if(mouth == Special){
        if(year % 4 == 0){
            29
        }else{
            28
        }
    }else{
        30
    }
}