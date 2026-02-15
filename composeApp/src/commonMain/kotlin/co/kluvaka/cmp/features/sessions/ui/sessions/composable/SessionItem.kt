package co.kluvaka.cmp.features.sessions.ui.sessions.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.kluvaka.cmp.features.common.domain.DateFormatter
import co.kluvaka.cmp.features.sessions.domain.model.Session
import co.kluvaka.cmp.features.sessions.domain.model.totalFishCount
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.sessions_empty_state_background
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionItem(
  session: Session,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  onRemove: (Session) -> Unit,
) {
  val formattedDate = DateFormatter.format(session.dateMillis)
  Card(
    modifier = modifier
      .fillMaxWidth()
      .combinedClickable(
        onClick = onClick,
        onLongClick = { onRemove(session) },
      ),
  ) {
    val coverPhoto = session.coverPhoto?.takeIf { it.isNotEmpty() }
      ?: session.events
        .flatMap { it.photos }
        .firstOrNull { it.isNotEmpty() }
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
        date = formattedDate,
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

@Composable
fun SessionNewItem(
  session: Session,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  onRemove: (Session) -> Unit,
) {
  val formattedDate = DateFormatter.format(
    timestamp = session.dateMillis,
    pattern = "dd.mm.yyyy",
  )
  Card(
    modifier = modifier
      .fillMaxWidth()
      .combinedClickable(
        onClick = onClick,
        onLongClick = { onRemove(session) },
      ),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFF3A3D38),
    ),
    elevation = CardDefaults.cardElevation(4.dp),
  ) {
    Column {
      // Image section
      val coverPhoto = session.coverPhoto?.takeIf { it.isNotEmpty() }
        ?: session
          .events
          .flatMap { it.photos }
          .firstOrNull { it.isNotEmpty() }
      val placeholder = painterResource(Res.drawable.sessions_empty_state_background)
      Box {
        AsyncImage(
          model = coverPhoto,
          contentDescription = null,
          placeholder = placeholder,
          error = placeholder,
          fallback = placeholder,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f),
        )

//        // Badge (top-right)
//        Box(
//          modifier = Modifier
//            .padding(8.dp)
//            .size(36.dp)
//            .align(Alignment.TopEnd)
//            .background(
//              color = Color(0xFF6B7C3E),
//              shape = RoundedCornerShape(8.dp),
//            ),
//          contentAlignment = Alignment.Center,
//        ) {
//          Icon(
//            imageVector = Icons.Default.PanoramaFishEye,
//            contentDescription = null,
//            tint = Color.White,
//            modifier = Modifier.size(18.dp),
//          )
//        }
      }

      // Content
      Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Text(
          text = session.location,
          style = MaterialTheme.typography.titleMedium,
          color = Color.White,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          InfoChip(
            icon = Icons.Default.CalendarMonth,
            text = formattedDate,
          )
          InfoChip(
            icon = Icons.Default.SetMeal,
            text = session.events.totalFishCount().toString(),
          )
        }

        Text(
          text = session.notes.orEmpty(),
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFFBDBDBD),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Composable
private fun InfoChip(
  icon: ImageVector,
  text: String,
) {
  Row(
    modifier = Modifier
      .background(
        color = Color(0xFF2F322E),
        shape = RoundedCornerShape(8.dp),
      )
      .padding(horizontal = 8.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = Color.White,
      modifier = Modifier.size(14.dp),
    )
    Text(
      text = text,
      color = Color.White,
      style = MaterialTheme.typography.labelSmall,
    )
  }
}
