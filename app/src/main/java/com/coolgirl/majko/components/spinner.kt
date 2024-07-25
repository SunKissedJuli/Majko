package com.coolgirl.majko.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SpinnerSample(
    name: String,
    items: List<SpinnerItems>,
    selectedItem: String,
    onChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelectedItem by remember { mutableStateOf(selectedItem) }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = name + " " + currentSelectedItem, fontSize = 18.sp)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()) {
            items.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colors.secondary),
                    onClick = {
                        expanded = false
                        currentSelectedItem = item.Name
                        onChange(item.Id)})
                { Text(item.Name) }
            }
        }
        Icon(imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "",
            modifier = Modifier
                .height(24.dp)
                .clickable { expanded = true })
    }
}

data class SpinnerItems(
    val Id:String,
    val Name:  String
)