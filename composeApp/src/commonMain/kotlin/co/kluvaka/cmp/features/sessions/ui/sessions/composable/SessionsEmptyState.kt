package co.kluvaka.cmp.features.sessions.ui.sessions.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.sessions_empty_state
import kluvaka.composeapp.generated.resources.sessions_empty_state_background
import kluvaka.composeapp.generated.resources.sessions_empty_state_content_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SessionsEmptyState(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      modifier = Modifier.padding(horizontal = 24.dp),
      text = stringResource(Res.string.sessions_empty_state),
      textAlign = TextAlign.Center,
    )
    Image(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 64.dp),
      painter = painterResource(Res.drawable.sessions_empty_state_background),
      contentDescription = stringResource(Res.string.sessions_empty_state_content_description),
      contentScale = ContentScale.Crop,
    )
  }
}