package co.kluvaka.cmp.features.equipment.ui.add.equipment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.Actions.AddCameraImage
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.Actions.AddGalleryImages
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.Actions.DeleteImage
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.Actions.EditEquipment
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.Actions.PriceUpdate
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.Actions.Save
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentOperation.Actions.TitleUpdate
import co.kluvaka.cmp.features.trophies.domain.rememberPhotoPicker
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.equipment_name_label
import kluvaka.composeapp.generated.resources.equipment_photo_title
import kluvaka.composeapp.generated.resources.equipment_price_label
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

data class AddEquipmentScreen(
  private val equipmentId: Int? = null,
) : Screen {

  data class Actions(
    val onEditEquipment: (Int) -> Unit,
    val onImageDelete: (Int) -> Unit,
    val onNavigateBack: () -> Unit,
    val onOpenCamera: () -> Unit,
    val onOpenGallery: () -> Unit,
    val onPriceUpdate: (String) -> Unit,
    val onSave: () -> Unit,
    val onTitleUpdate: (String) -> Unit,
  ) {
    companion object {
      val Empty = Actions(
        onEditEquipment = {},
        onImageDelete = {},
        onNavigateBack = {},
        onOpenCamera = {},
        onOpenGallery = {},
        onPriceUpdate = {},
        onSave = {},
        onTitleUpdate = {},
      )
    }
  }

  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<AddEquipmentViewModel>()
    val photoPicker = rememberPhotoPicker()
    val state by viewModel.state.collectAsState()

    val actions = Actions(
      onEditEquipment = { id -> viewModel.handleAction(EditEquipment(id)) },
      onImageDelete = { index -> viewModel.handleAction(DeleteImage(index))  },
      onNavigateBack = { navigator?.pop() },
      onOpenCamera = {
        photoPicker.pickFromCamera { imageUri ->
          viewModel.handleAction(AddCameraImage(imageUri))
        }
      },
      onOpenGallery = {
        photoPicker.pickMultipleFromGallery { imageUris ->
          viewModel.handleAction(AddGalleryImages(imageUris))
        }
      },
      onPriceUpdate = { price -> viewModel.handleAction(PriceUpdate(price)) },
      onSave = {
        viewModel.handleAction(Save)
        navigator?.pop()
      },
      onTitleUpdate = { title -> viewModel.handleAction(TitleUpdate(title)) },
    )

    LaunchedEffect(equipmentId) {
      equipmentId?.let { actions.onEditEquipment(it) }
    }

    AddEquipmentScreenContent(
      state = state,
      actions = actions,
    )
  }
}

@Composable
private fun AddEquipmentScreenContent(
  state: AddEquipmentState,
  actions: AddEquipmentScreen.Actions,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .imePadding(),
    verticalArrangement = Arrangement.Top
  )  {
    AddEquipmentTopBar { actions.onNavigateBack() }
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.Top,
    ) {
      // Text input section
      OutlinedTextField(
        value = state.input.title,
        onValueChange = actions.onTitleUpdate,
        label = { Text(stringResource(Res.string.equipment_name_label)) },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.padding(12.dp))
      OutlinedTextField(
        value = state.input.price,
        onValueChange = actions.onPriceUpdate,
        label = { Text(stringResource(Res.string.equipment_price_label)) },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Number,
        ),
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.padding(12.dp))

      // Photo picker section
      Text(
        text = stringResource(Res.string.equipment_photo_title),
        style = MaterialTheme.typography.titleMedium
      )
      MediaSelection(
        onOpenCamera = actions.onOpenCamera,
        onOpenGallery = actions.onOpenGallery,
      )

      // Display selected images
      if (state.input.images.isNotEmpty()) {
        ImagesList(
          images = state.input.images,
          onImageDelete = actions.onImageDelete,
        )
      }
      Spacer(modifier = Modifier.weight(1f))
      SaveButton(
        enabled = state.input.title.isNotBlank(),
        onClick = actions.onSave,
      )
    }
  }
}
