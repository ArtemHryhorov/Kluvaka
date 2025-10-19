package co.kluvaka.cmp.features.sessions.ui.history.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.kluvaka.cmp.features.sessions.domain.model.FishingSession

@Composable
fun FishingSessionList(
  sessions: List<FishingSession>,
  onSessionClick: ((FishingSession) -> Unit)? = null
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    items(
      items = sessions,
      key = { it.id ?: it.hashCode() }
    ) { session ->
      FishingSessionCard(
        session = session,
        onClick = { onSessionClick?.invoke(session) },
      )
    }
  }
}