package co.kluvaka.cmp.features.equipment.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.equipment.domain.model.Equipment
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentScreen
import co.kluvaka.cmp.features.equipment.ui.details.EquipmentDetailsOperation.Action
import co.kluvaka.cmp.features.photos.ui.DetailedPhotoViewScreen
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.price_format
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

class EquipmentDetailsScreen(
  private val equipmentId: Int
) : Screen {

  data class Actions(
    val onNavigateBack: () -> Unit,
    val onNavigateToEditEquipmentClick: (Int) -> Unit,
    val onNavigateToImageDetails: (Int, List<String>) -> Unit,
  ) {
    companion object {
      val Empty = Actions(
        onNavigateBack = {},
        onNavigateToEditEquipmentClick = {},
        onNavigateToImageDetails = { _, _ -> },
      )
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<EquipmentDetailsViewModel>()
    val state by viewModel.state.collectAsState()

    val actions = Actions(
      onNavigateBack = { navigator?.pop() },
      onNavigateToEditEquipmentClick = { equipmentId ->
        navigator?.push(AddEquipmentScreen(equipmentId))
      },
      onNavigateToImageDetails = { index, images ->
        navigator?.push(DetailedPhotoViewScreen(images, index))
      }
    )

    // TODO: Delete when migrated to flow
    LaunchedEffect(equipmentId) {
      viewModel.handleAction(Action.GetEquipment(equipmentId))
    }

    state.equipment?.let { equipment ->
      EquipmentDetailsScreenContent(
        actions = actions,
        equipment = equipment,
      )
    }
  }
}

@Composable
private fun EquipmentDetailsScreenContent(
  actions: EquipmentDetailsScreen.Actions,
  equipment: Equipment,
) {
  Scaffold(
    topBar = {
      EquipmentDetailsTopBar(
        title = equipment.title,
        onNavigateBackClick = actions.onNavigateBack,
        onNavigateToAddEquipmentClick = { actions.onNavigateToEditEquipmentClick(equipment.id) },
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = paddingValues.calculateTopPadding())
        .verticalScroll(rememberScrollState()),
    ) {
      Spacer(modifier = Modifier.height(16.dp))
      Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = stringResource(Res.string.price_format, equipment.price),
          style = MaterialTheme.typography.bodyMedium
        )
      }

      if (equipment.images.isNotEmpty()) {
        EquipmentDetailsImagesSection(
          images = equipment.images,
          onImageClick = { index ->
            actions.onNavigateToImageDetails(index, equipment.images)
          },
        )
      }
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
