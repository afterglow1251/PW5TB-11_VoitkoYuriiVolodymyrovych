package com.example.calculator5_

import androidx.compose.runtime.Composable
import androidx.compose.material3.*

@Composable
fun ErrorDialog(
    onDismiss: () -> Unit,
    message: String = "Перевірте введені значення, всі поля повинні бути числами.",
    errorMessage: String = "Помилка",
    closeMessage: String = "Закрити"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(errorMessage) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(closeMessage)
            }
        }
    )
}