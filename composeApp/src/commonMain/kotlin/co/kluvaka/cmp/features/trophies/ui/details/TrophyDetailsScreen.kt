package co.kluvaka.cmp.features.trophies.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.photos.ui.DetailedPhotoViewScreen
import co.kluvaka.cmp.features.trophies.domain.model.Trophy
import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyScreen
import co.kluvaka.cmp.features.trophies.ui.details.TrophyDetailsOperation.Action
import org.koin.compose.viewmodel.koinViewModel

class TrophyDetailsScreen(
  private val trophyId: Int,
) : Screen {

  data class Actions(
    val onNavigateBack: () -> Unit,
    val onNavigateToEditTrophyClick: (Trophy) -> Unit,
    val onNavigateToImageDetails: (Int, List<String>) -> Unit,
  ) {
    companion object {
      val Empty = Actions(
        onNavigateBack = {},
        onNavigateToEditTrophyClick = {},
        onNavigateToImageDetails = { _, _ -> },
      )
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<TrophyDetailsViewModel>()
    val state by viewModel.state.collectAsState()

    val actions = Actions(
      onNavigateBack = { navigator?.pop() },
      onNavigateToEditTrophyClick = { trophy -> navigator?.push(AddTrophyScreen(trophy)) },
      onNavigateToImageDetails = { index, images ->
        navigator?.push(DetailedPhotoViewScreen(images, index))
      },
    )

    LaunchedEffect(trophyId) {
      viewModel.handleAction(Action.GetTrophy(trophyId))
    }

    state.trophy?.let { trophy ->
      TrophyDetailsScreenContent(
        actions = actions,
        trophy = trophy,
      )
    }
  }
}

@Composable
private fun TrophyDetailsScreenContent(
  actions: TrophyDetailsScreen.Actions,
  trophy: Trophy,
) {
  Scaffold(
    topBar = {
      TrophyDetailsTopAppBar(
        title = trophy.fishType,
        onNavigateBack = actions.onNavigateBack,
        onNavigateToEdit = { actions.onNavigateToEditTrophyClick(trophy) },
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
      TrophyDataColumn(trophy = trophy)
      if (trophy.images.isNotEmpty()) {
        MediaSection(
          images = trophy.images,
          onImageClick = { index ->
            actions.onNavigateToImageDetails(index, trophy.images)
          },
        )
      }
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
