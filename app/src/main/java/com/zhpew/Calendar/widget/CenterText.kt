package com.zhpew.Calendar.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment

@Composable
public fun GravityText(
    modifier: Modifier,
    textStyle: TextStyle,
    content: String,
    vertical: Arrangement.Vertical = Arrangement.Top,
    horizontal: Alignment.Horizontal = Alignment.Start
) {
    Column(modifier = modifier, verticalArrangement = vertical, horizontalAlignment = horizontal) {
        Text(text = content, style = textStyle)
    }
}