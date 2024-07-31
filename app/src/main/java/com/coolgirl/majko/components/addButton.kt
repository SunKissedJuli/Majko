package com.coolgirl.majko.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.coolgirl.majko.R

@Composable
fun AddButton(onClick: (String) -> Unit, id: String){

    IconButton(onClick = { onClick(id)},
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 25.dp)
            .size(56.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary)
        ) {
        Image(painter = painterResource(R.drawable.icon_plus), contentDescription = "")
    }
}