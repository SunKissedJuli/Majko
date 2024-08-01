package com.coolgirl.majko.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.coolgirl.majko.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlinePickerWithText(
    currentDeadline: String,
    onUpdateDeadline: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val formattedDisplayDate = remember(currentDeadline) {
        if (currentDeadline.isNotEmpty()) {
            val dateTime = LocalDateTime.parse(currentDeadline, DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"))
            dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")) +
                    ", " + dateTime.dayOfMonth + " " +
                    dateTime.month.getDisplayName(TextStyle.FULL, Locale("ru"))

        } else{ currentDeadline }
    }

    Text(text = stringResource(R.string.taskeditor_deadline) + " " + formattedDisplayDate,
        fontSize = 18.sp, modifier = Modifier.clickable { showDialog = true })

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.ofEpochMilli(millis)
                        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                        val formattedSaveDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"))
                        onUpdateDeadline(formattedSaveDate)
                    }
                    showDialog = false }) {
                    Text(stringResource(R.string.message_ok))
                }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) {
                Text(stringResource(R.string.common_cansel)) }
            }) {
            DatePicker(state = datePickerState)
        }
    }
}