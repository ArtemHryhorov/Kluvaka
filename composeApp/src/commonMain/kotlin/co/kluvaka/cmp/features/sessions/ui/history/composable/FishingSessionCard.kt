package co.kluvaka.cmp.features.sessions.ui.history.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishingSessionCard(
  session: FishingSession,
  onClick: (() -> Unit)? = null,
  onRemove: (FishingSession) -> Unit,
) {
  val swipeState = rememberSwipeToDismissBoxState(
    confirmValueChange = { value ->
      if (value == SwipeToDismissBoxValue.EndToStart) {
        onRemove(session)
        false
      } else {
        value != SwipeToDismissBoxValue.StartToEnd
      }
    }
  )

  SwipeToDismissBox(
    state = swipeState,
    modifier = Modifier.fillMaxWidth(),
    enableDismissFromStartToEnd = false,
    backgroundContent = {
      when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                color = Color.Red,
                shape = RoundedCornerShape(12.dp),
              )
              .wrapContentSize(Alignment.CenterEnd)
              .padding(end = 16.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Delete session",
              tint = Color.White
            )
          }
        }
        SwipeToDismissBoxValue.StartToEnd -> {}
        SwipeToDismissBoxValue.Settled -> {}
      }
    }
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clickable(enabled = onClick != null) {
          onClick?.invoke()
        },
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
      colors = CardDefaults.cardColors(
        containerColor = if (session.isActive) {
          Color(0xFF8ADB6B)
        } else {
          MaterialTheme.colorScheme.surface
        },
      )
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = session.location,
          style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = session.date,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = if (session.isActive) "Статус: Активная" else "Статус: Завершена",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}