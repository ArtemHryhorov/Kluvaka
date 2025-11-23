package co.kluvaka.cmp.features.equipment.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.viewmodel.koinViewModel

class EquipmentDetailsScreen(
  private val equipmentId: Int
) : Screen {

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<EquipmentDetailsViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(equipmentId) {
      viewModel.loadEquipment(equipmentId)
    }

    Scaffold(
      topBar = {
        TopAppBar(
          windowInsets = WindowInsets(0, 0, 0, 0),
          title = { Text(state?.title ?: "Детали приблуды") },
          navigationIcon = {
            IconButton(onClick = { navigator?.pop() }) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
          },
          colors = TopAppBarDefaults.topAppBarColors()
        )
      }
    ) { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(top = paddingValues.calculateTopPadding())
          .padding(horizontal = 16.dp)
          .verticalScroll(rememberScrollState()),
      ) {
        state?.let { equipment ->
          Spacer(modifier = Modifier.height(16.dp))
          Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "Стоимость: ${equipment.price} ГРН",
              style = MaterialTheme.typography.headlineMedium
            )

            equipment.image?.let { image ->
              Spacer(modifier = Modifier.padding(8.dp))
              Text(
                text = "Фото приблуды:",
                style = MaterialTheme.typography.titleMedium
              )
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(color = Color.Transparent),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  painter = rememberAsyncImagePainter(image),
                  contentDescription = "Trophy photo",
                  modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                  contentScale = ContentScale.FillWidth,
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}