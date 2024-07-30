package com.coolgirl.majko.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.MaterialTheme

@Composable
fun MessageSnackbar(
    message: Int,
    onDismiss: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val messageString = stringResource(id = message)

    LaunchedEffect(message) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = messageString,
                duration = SnackbarDuration.Short
            )
            delay(2000)
            onDismiss()
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier,
    ) { data ->
        Snackbar(
            action = {
                TextButton(onClick = { data.dismiss() }) {
                    Text("OK", color = MaterialTheme.colors.onSecondary)
                }
            },
            containerColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSecondary,
        ) {
            Text(text = data.visuals.message)
        }
    }
}
