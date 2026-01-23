package co.kluvaka.cmp.features.sessions.ui.sessions.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.kluvaka.cmp.features.sessions.domain.model.Session

@Composable
fun SessionsListContent(
  sessions: List<Session>,
  onSessionClick: ((Session) -> Unit)? = null,
  onSessionDelete: (Session) -> Unit = {},
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    items(sessions) { session ->
      SessionItem(
        session = session,
        onClick = { onSessionClick?.invoke(session) },
        onRemove = { onSessionDelete(session) },
      )
    }
  }
}