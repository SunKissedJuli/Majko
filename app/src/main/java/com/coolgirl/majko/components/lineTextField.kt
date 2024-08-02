package com.coolgirl.majko.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.coolgirl.majko.R

@Composable
fun LineTextField(value: String, onValueChange: (String)-> Unit, placeholder: String = "", modifier: Modifier = Modifier){
    TextField(
        value = value,
        modifier = modifier.height(55.dp),
        placeholder = { Text(text = placeholder, color = MaterialTheme.colors.onSurface) },
        onValueChange = { onValueChange(it) },
        colors = TextFieldDefaults.colors(focusedContainerColor = colorResource(R.color.white),
            unfocusedContainerColor = MaterialTheme.colors.background)
    )
}