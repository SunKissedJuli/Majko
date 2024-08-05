package com.coolgirl.majko.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun FilterDropdown(
    expanded: Boolean,
    onDismissRequest: (Boolean) -> Unit,
    firstText: Int,
    onClick: (Int) -> Unit,
    secondText: Int,
    thirdText: Int,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest(false) },
        modifier = Modifier.fillMaxWidth(0.6f)
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(firstText)) },
            onClick = {
                onClick(1)
                onDismissRequest(false)
            }
        )

        DropdownMenuItem(
            text = { Text(stringResource(secondText)) },
            onClick = {
                onClick(0)
                onDismissRequest(false)
            }
        )

        DropdownMenuItem(
            text = { Text(stringResource(thirdText)) },
            onClick = {
                onClick(2)
                onDismissRequest(false)
            }
        )
    }
}
