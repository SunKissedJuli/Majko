package com.coolgirl.majko.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun FilterBox(onDismissRequest: ()-> Unit, ){
    Dialog(onDismissRequest = { onDismissRequest }) {
        

    }
}