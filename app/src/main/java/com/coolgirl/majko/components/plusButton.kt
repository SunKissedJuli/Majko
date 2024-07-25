package com.coolgirl.majko.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.coolgirl.majko.R

@Composable
fun plusButton(onClick: (String) -> Unit, id: String){

    Button(onClick = { onClick(id)},
        shape = CircleShape,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 25.dp)
            .size(56.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
        Image(painter = painterResource(R.drawable.icon_plus), contentDescription = "")
    }
}