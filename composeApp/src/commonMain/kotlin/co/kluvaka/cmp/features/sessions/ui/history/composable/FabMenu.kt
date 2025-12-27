package co.kluvaka.cmp.features.sessions.ui.history.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun FabMenu(
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
)
