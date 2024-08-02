package com.coolgirl.majko.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.coolgirl.majko.R

@Composable
fun ButtonBack(onClick: ()-> Unit){
    IconButton(onClick = onClick ) {
        Icon(painter = painterResource(R.drawable.icon_back), contentDescription = "",
            tint = MaterialTheme.colorScheme.background)
    }
}