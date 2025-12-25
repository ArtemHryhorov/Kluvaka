package co.kluvaka.cmp.features.equipment.ui.equipments

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
import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import coil3.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EquipmentsTopBar(totalPrice: Double) {
  TopAppBar(
    windowInsets = WindowInsets(0, 0, 0, 0),
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = "Мой Арсенал",
        )
        Text(
          modifier = Modifier.padding(end = 16.dp),
          text = "₴$totalPrice",
        )
      }
    }
  )
}

@Composable
internal fun EquipmentItem(
  equipment: Equipment,
  onClick: (Equipment) -> Unit,
  onRemove: (Equipment) -> Unit,
  modifier: Modifier = Modifier,
) {
  val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
    confirmValueChange = {
      if (it == SwipeToDismissBoxValue.EndToStart) {
        onRemove(equipment)
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
              .background(MaterialTheme.colorScheme.error)
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
    EquipmentCard(
      item = equipment,
      onClick = { onClick(equipment) },
    )
  }
}

@Composable
internal fun EquipmentCard(
  item: Equipment,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    onClick = onClick,
    modifier = modifier.fillMaxWidth(),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Column(
        modifier = Modifier
          .fillMaxHeight()
          .weight(1f),
        verticalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = item.title,
          style = MaterialTheme.typography.titleMedium,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
          text = "₴${item.price}",
          style = MaterialTheme.typography.bodyMedium,
        )
      }
      Spacer(modifier = Modifier.size(4.dp))
      Image(
        painter = rememberAsyncImagePainter(item.images.firstOrNull()),
        contentDescription = "Trophy photo",
        modifier = Modifier
          .size(64.dp)
          .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
      )
    }
  }
}
