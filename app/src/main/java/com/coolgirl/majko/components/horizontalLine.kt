package com.coolgirl.majko.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalLine(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp, bottom = 12.dp)
        .height(1.dp)
        .background(color = MaterialTheme.colorScheme.onSurface)
    )
}