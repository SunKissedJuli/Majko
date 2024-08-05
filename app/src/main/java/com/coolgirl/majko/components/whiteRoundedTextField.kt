package com.coolgirl.majko.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WhiteRoundedTextField(value: String, onValueChange: (String)-> Unit, plaseholder: String,
                          modifier: Modifier = Modifier){
    OutlinedTextField(
            value = value,
    onValueChange = {onValueChange(it)},
    modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
    shape = RoundedCornerShape(30.dp),
    colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background, unfocusedContainerColor = MaterialTheme.colorScheme.background,
        focusedBorderColor = MaterialTheme.colorScheme.background, unfocusedBorderColor = MaterialTheme.colorScheme.background),
    placeholder = { Text(text = plaseholder, color = MaterialTheme.colorScheme.onSurface) })
}