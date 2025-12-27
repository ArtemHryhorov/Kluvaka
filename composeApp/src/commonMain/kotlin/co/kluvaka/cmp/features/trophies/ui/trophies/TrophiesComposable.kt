package co.kluvaka.cmp.features.trophies.ui.trophies

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.kluvaka.cmp.features.common.domain.DateFormatter
import co.kluvaka.cmp.features.trophies.domain.model.Trophy
import coil3.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TrophiesTopBar() {
  TopAppBar(
    windowInsets = WindowInsets(0, 0, 0, 0),
    title = {
      Text(
        text = "Мои Трофеи",
      )
    }
  )
}

@Composable
internal fun TrophyItem(
  trophy: Trophy,
  onRemove: (Trophy) -> Unit,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
    confirmValueChange = {
      if (it == SwipeToDismissBoxValue.EndToStart) {
        onRemove(trophy)
        false
      } else {
        it != SwipeToDismissBoxValue.StartToEnd
      }
    }
  )

  SwipeToDismissBox(
    state = swipeToDismissBoxState,
    modifier = modifier.fillMaxSize(),
    enableDismissFromStartToEnd = false,
    backgroundContent = {
      when (swipeToDismissBoxState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Remove item",
            modifier = Modifier
              .fillMaxSize()
              .background(
                color = MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(8.dp),
              )
              .wrapContentSize(Alignment.CenterEnd)
              .padding(12.dp),
            tint = MaterialTheme.colorScheme.onError
          )
        }

        SwipeToDismissBoxValue.StartToEnd -> {}
        SwipeToDismissBoxValue.Settled -> {}
      }
    }
  ) {
    TrophyCard(trophy, onClick)
  }
}

@Composable
internal fun TrophyCard(
  trophy: Trophy,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    onClick = onClick,
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        modifier = Modifier
          .fillMaxHeight()
          .weight(1f),
      ) {
        Text(
          text = trophy.fishType,
          style = MaterialTheme.typography.titleMedium,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        trophy.weight?.let { weight ->
          Text(
            text = "Вес: $weight кг",
            style = MaterialTheme.typography.bodyMedium,
          )
        }
        trophy.length?.let { length ->
          Text(
            text = "Длина: $length см",
            style = MaterialTheme.typography.bodyMedium,
          )
        }
        trophy.location?.let { location ->
          Text(
            text = "Место: $location",
            style = MaterialTheme.typography.bodyMedium,
          )
        }
        trophy.date?.let { date ->
          Text(
            text = "Дата: ${DateFormatter.format(date)}",
            style = MaterialTheme.typography.bodyMedium,
          )
        }
      }
      Spacer(modifier = Modifier.size(4.dp))
      trophy.images.firstOrNull()?.let { image ->
        Image(
          painter = rememberAsyncImagePainter(image),
          contentDescription = "Trophy photo",
          modifier = Modifier
            .size(96.dp)
            .clip(RoundedCornerShape(8.dp)),
          contentScale = ContentScale.Crop
        )
      }
    }
  }
}
