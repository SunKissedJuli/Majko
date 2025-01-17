package com.coolgirl.majko.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import com.coolgirl.majko.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ErrorSnackbar(
    errorMessage: Int,
    onDismiss: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val messageString = stringResource(id = errorMessage)

    LaunchedEffect(errorMessage) {
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
                    Text(stringResource(R.string.message_ok), color = MaterialTheme.colorScheme.onSecondary)
                }
            },
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.alpha(0.9f)
        ) {
            Text(text = data.visuals.message)
        }
    }
}
