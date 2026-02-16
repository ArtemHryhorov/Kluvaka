package co.kluvaka.cmp.features.sessions.ui.sessions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.foundation.layout.padding
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.start_session
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun FabMenu(
  modifier: Modifier,
  onClick: () -> Unit,
) {
  FloatingActionButton(
    modifier = modifier
      .padding(all = 16.dp)
      .zIndex(3f),
    onClick = onClick,
  ) {
    Icon(
      imageVector = Icons.Default.Add,
      contentDescription = stringResource(Res.string.start_session),
    )
  }
}

