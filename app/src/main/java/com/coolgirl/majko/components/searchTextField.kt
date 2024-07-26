package com.coolgirl.majko.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coolgirl.majko.R

@Composable
fun SearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: Int,
) {
    Row(Modifier.fillMaxWidth(0.7f), verticalAlignment = Alignment.CenterVertically){
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            textStyle = TextStyle.Default.copy(fontSize = 17.sp, color = MaterialTheme.colors.background),
            decorationBox = { innerTextField ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (value.isEmpty()) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.CenterStart)) {
                            Text(text = stringResource(placeholder), fontSize = 17.sp)
                        }
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.width(5.dp))



             /*   Icon(painter = painterResource(R.drawable.icon_filter), modifier = Modifier.clickable {  },
                    contentDescription = "", tint = MaterialTheme.colors.surface)

             Spacer(modifier = Modifier.width(5.dp))

                Icon(painter = painterResource(R.drawable.icon_filter_off),
                    contentDescription = "", tint = MaterialTheme.colors.surface)*/

    }

}