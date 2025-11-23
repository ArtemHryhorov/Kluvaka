package co.kluvaka.cmp.features.equipment.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.equipment.ui.add.equipment.AddEquipmentScreen
import co.kluvaka.cmp.features.photos.ui.DetailedPhotoViewScreen
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
    val equipment by viewModel.equipment.collectAsState()

    LaunchedEffect(equipmentId) {
      viewModel.loadEquipment(equipmentId)
    }

    Scaffold(
      topBar = {
        TopAppBar(
          windowInsets = WindowInsets(0, 0, 0, 0),
          title = { Text(equipment?.title ?: "Детали приблуды") },
          navigationIcon = {
            IconButton(onClick = { navigator?.pop() }) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
          },
          actions = {
            IconButton(
              onClick = {
                navigator?.push(AddEquipmentScreen(equipment))
              },
            ) {
              Icon(Icons.Filled.Edit, contentDescription = "Edit")
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
          .verticalScroll(rememberScrollState()),
      ) {
        equipment?.let { equipment ->
          Spacer(modifier = Modifier.height(16.dp))
          Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "Стоимость: ${equipment.price} ГРН",
              style = MaterialTheme.typography.bodyMedium
            )
          }

          if (equipment.images.isNotEmpty()) {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
              text = "Фото приблуды:",
              modifier = Modifier.padding(horizontal = 16.dp),
              style = MaterialTheme.typography.titleMedium
            )
            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              item {
                Spacer(modifier = Modifier.width(16.dp))
              }
              items(equipment.images.size) { index ->
                val image = equipment.images[index]
                Box(
                  modifier = Modifier
                    .height(200.dp)
                    .width(300.dp),
                ) {
                  Card(
                    onClick = {
                      navigator?.push(
                        DetailedPhotoViewScreen(
                          images = equipment.images,
                          initialIndex = index,
                        )
                      )
                    },
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Image(
                      painter = rememberAsyncImagePainter(image),
                      contentDescription = "Equipment photo",
                      modifier = Modifier.fillMaxSize(),
                      contentScale = ContentScale.Crop,
                    )
                  }
                }
              }
              item {
                Spacer(modifier = Modifier.width(16.dp))
              }
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}