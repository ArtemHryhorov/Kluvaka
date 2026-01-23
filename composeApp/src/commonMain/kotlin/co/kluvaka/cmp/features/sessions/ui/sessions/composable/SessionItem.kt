package co.kluvaka.cmp.features.sessions.ui.sessions.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PanoramaFishEye
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.model.totalFishCount
import co.kluvaka.cmp.features.sessions.domain.model.totalFishWeight
import coil3.compose.rememberAsyncImagePainter
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.delete_session_content_description
import kluvaka.composeapp.generated.resources.fish_caught_count_format
import kluvaka.composeapp.generated.resources.fish_caught_format
import kluvaka.composeapp.generated.resources.fish_caught_weight_format
import kluvaka.composeapp.generated.resources.session_status_active
import kluvaka.composeapp.generated.resources.session_status_completed
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionItem(
  session: Session,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  onRemove: (Session) -> Unit,
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .combinedClickable(
        onClick = onClick,
        onLongClick = { onRemove(session) },
      ),
  ) {
    val coverPhoto = session.events.first().photos.first()
    Image(
      painter = rememberAsyncImagePainter(coverPhoto),
      contentDescription = null,
      modifier = Modifier
        .fillMaxWidth()
        .height(128.dp)
        .clip(
          RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
          )
        ),
      contentScale = ContentScale.Crop
    )
    Text(
      modifier = Modifier.padding(
        start = 8.dp,
        end = 8.dp,
        top = 12.dp,
      ),
      text = session.location,
      style = MaterialTheme.typography.titleMedium,
      overflow = TextOverflow.Ellipsis,
    )
    Row {
      DateRow(
        date = session.date,
      )
      FishCountRow(
        count = session.events.totalFishCount(),
      )
    }
  }
}

@Composable
private fun DateRow(
  date: String,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
  ) {
    Icon(
      imageVector = Icons.Default.CalendarMonth,
      contentDescription = null,
    )
    Text(
      text = date,
      style = MaterialTheme.typography.labelSmall,
    )
  }
}

@Composable
private fun FishCountRow(
  count: Int,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
  ) {
    Icon(
      imageVector = Icons.Default.PanoramaFishEye,
      contentDescription = null,
    )
    Text(
      text = count.toString(),
      style = MaterialTheme.typography.labelSmall,
    )
  }
}
