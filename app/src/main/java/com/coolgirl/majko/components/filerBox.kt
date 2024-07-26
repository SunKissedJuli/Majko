package com.coolgirl.majko.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun FilterDropdown(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    firstText: Int,
    onFirstClick:()-> Unit,
    secondText: Int,
    onSecondClick:()-> Unit,
    thirdText: Int,
    onThirdClick:()-> Unit
    // ... другие параметры для настройки фильтрации
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) },
        modifier = Modifier.width(300.dp) // Настройте ширину по необходимости
    ) {

        DropdownMenuItem(onClick = { onFirstClick() }) {
            Text(stringResource(firstText))
        }
        DropdownMenuItem(onClick = { onSecondClick() }) {
            Text(stringResource(secondText))
        }

        DropdownMenuItem(onClick = { onThirdClick() }) {
            Text(stringResource(thirdText))
        }
    }
}