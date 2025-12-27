package co.kluvaka.cmp.features.equipment.ui.equipments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.add_equipment_button
import kluvaka.composeapp.generated.resources.cancel
import kluvaka.composeapp.generated.resources.delete_equipment_dialog_confirm
import kluvaka.composeapp.generated.resources.delete_equipment_dialog_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

object EquipmentsScreen : Screen {

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

  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<EquipmentsViewModel>()
    val state by viewModel.state.collectAsState()

    val actions = Actions(
      onAddEquipmentClick = { navigator?.push(AddEquipmentScreen()) },
      onDeleteCancel = { viewModel.handleAction(DeleteEquipmentCancel) },
      onDeleteConfirm = { id -> viewModel.handleAction(DeleteEquipmentConfirm(id)) },
      onDeleteRequest = { equipment -> viewModel.handleAction(DeleteEquipmentRequest(equipment)) },
      onEquipmentClick = { id -> navigator?.push(EquipmentDetailsScreen(id)) },
    )

    // TODO: Delete when migrated to flow
    LaunchedEffect(Unit) {
      viewModel.handleAction(FetchEquipments)
    }

    EquipmentsScreenContent(
      state = state,
      actions = actions,
    )
  }
}

@Composable
private fun EquipmentsScreenContent(
  state: EquipmentsState,
  actions: EquipmentsScreen.Actions,
) {
  (state.deleteConfirmationDialog as? DialogState.Shown<Equipment>)?.value?.let { equipment ->
    Dialog(
      title = stringResource(Res.string.delete_equipment_dialog_title, equipment.title),
      cancelButtonText = stringResource(Res.string.cancel),
      confirmButtonText = stringResource(Res.string.delete_equipment_dialog_confirm),
      onConfirmClick = { actions.onDeleteConfirm(equipment.id) },
      onDismissClick = actions.onDeleteCancel,
    )
  }

  Column(
    modifier = Modifier.fillMaxSize(),
  ) {
    EquipmentsTopBar(totalPrice = state.totalPrice)
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
        onClick = actions.onAddEquipmentClick,
      ) {
        Text(
          modifier = Modifier.padding(horizontal = 16.dp),
          text = stringResource(Res.string.add_equipment_button),
        )
      }
    }
  }
}
