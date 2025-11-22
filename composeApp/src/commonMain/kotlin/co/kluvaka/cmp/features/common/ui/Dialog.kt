package co.kluvaka.cmp.features.common.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun Dialog(
  title: String? = null,
  description: String? = null,
  confirmButtonText: String = "OK",
  cancelButtonText: String? = null,
  onConfirmClick: () -> Unit,
  onDismissClick: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismissClick,
    title = title?.let { { Text(text = it) } },
    text = description?.let { { Text(text = it) } },
    confirmButton = {
      Button(onClick = onConfirmClick) {
        Text(text = confirmButtonText)
      }
    },
    dismissButton = cancelButtonText?.let {
      {
        TextButton(onClick = onDismissClick) {
          Text(text = it)
        }
      }
    }
  )
}