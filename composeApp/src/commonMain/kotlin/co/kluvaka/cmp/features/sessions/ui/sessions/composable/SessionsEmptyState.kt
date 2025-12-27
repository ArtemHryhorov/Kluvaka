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
import kluvaka.composeapp.generated.resources.sessions_empty_state_background
import org.jetbrains.compose.resources.painterResource

@Composable
fun SessionsEmptyState(
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      modifier = Modifier.padding(horizontal = 64.dp),
      text = "У вас пока нет рыбалок\n Самое время добавить первую \uD83C\uDFA3",
      textAlign = TextAlign.Center,
    )
    Image(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 64.dp),
      painter = painterResource(Res.drawable.sessions_empty_state_background),
      contentDescription = "Sessions empty state image",
      contentScale = ContentScale.Crop,
    )
  }
}