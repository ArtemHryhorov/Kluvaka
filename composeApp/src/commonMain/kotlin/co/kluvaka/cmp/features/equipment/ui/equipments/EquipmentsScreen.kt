package co.kluvaka.cmp.features.equipment.ui.equipments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.common.ui.Dialog
import co.kluvaka.cmp.features.common.ui.DialogState
import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentScreen
import co.kluvaka.cmp.features.equipment.ui.details.EquipmentDetailsScreen
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsOperation.Actions.DeleteEquipmentCancel
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsOperation.Actions.DeleteEquipmentConfirm
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsOperation.Actions.DeleteEquipmentRequest
import co.kluvaka.cmp.features.equipment.ui.equipments.EquipmentsOperation.Actions.FetchEquipments
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.viewmodel.koinViewModel

object EquipmentsScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<EquipmentsViewModel>()
    val state by viewModel.state.collectAsState()

    // TODO: Delete when migrated to flow
    viewModel.handleAction(FetchEquipments)

    val actions = EquipmentScreen.Actions(
      onAddEquipmentClick = { navigator?.push(AddEquipmentScreen()) },
      onDeleteCancel = { viewModel.handleAction(DeleteEquipmentCancel) },
      onDeleteConfirm = { id -> viewModel.handleAction(DeleteEquipmentConfirm(id)) },
      onDeleteRequest = { equipment -> viewModel.handleAction(DeleteEquipmentRequest(equipment)) },
      onEquipmentClick = { id -> navigator?.push(EquipmentDetailsScreen(id)) },
    )

    EquipmentsScreenContent(
      state = state,
      actions = actions,
    )
  }
}

@Composable
private fun EquipmentsScreenContent(
  state: EquipmentsState,
  actions: EquipmentScreen.Actions,
) {
  (state.deleteConfirmationDialog as? DialogState.Shown<Equipment>)?.value?.let { equipment ->
    Dialog(
      title = "Удалить ${equipment.title} из Вашего арсенала?",
      cancelButtonText = "Отмена",
      confirmButtonText = "Да, удалить",
      onConfirmClick = { actions.onDeleteConfirm(equipment.id) },
      onDismissClick = actions.onDeleteCancel,
    )
  }

  Box(Modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
      EquipmentsTopBar(state.totalPrice)
      Box(
        modifier = Modifier.fillMaxSize(),
      ) {
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
          items(
            items = state.equipments,
            key = { it.id }
          ) { equipment ->
            EquipmentItem(
              equipment = equipment,
              onClick = { actions.onEquipmentClick(equipment.id) },
              onRemove = { actions.onDeleteRequest(equipment) },
            )
          }
        }
        FloatingActionButton(
          modifier = Modifier
            .padding(all = 16.dp)
            .align(Alignment.BottomEnd)
            .zIndex(3f),
          onClick = {
            actions.onAddEquipmentClick()
          },
        ) {
          Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Добавить",
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EquipmentsTopBar(totalPrice: Double) {
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
fun EquipmentItem(
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
private fun EquipmentCard(
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

private object EquipmentScreen {
  data class Actions(
    val onAddEquipmentClick: () -> Unit,
    val onDeleteCancel: () -> Unit,
    val onDeleteConfirm: (id: Int) -> Unit,
    val onDeleteRequest: (equipment: Equipment) -> Unit,
    val onEquipmentClick: (id: Int) -> Unit,
  ) {
    companion object {
      val Empty = Actions(
        onAddEquipmentClick = {},
        onDeleteCancel = {},
        onDeleteConfirm = {},
        onDeleteRequest = {},
        onEquipmentClick = {},
      )
    }
  }
}
