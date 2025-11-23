package co.kluvaka.cmp.features.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

import co.kluvaka.cmp.features.common.domain.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    value: Long,
    onDateSelected: (Long) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        val dateString = if (value > 0) {
            DateFormatter.format(value)
        } else {
            ""
        }

        OutlinedTextField(
            value = dateString,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.CalendarToday, contentDescription = "Select date")
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = false, // Disable text input, handle click on parent Box or use interaction source
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                disabledTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )

        // Workaround to make the disabled TextField clickable
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDatePicker = true }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = if (value > 0) value else null)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(millis)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

