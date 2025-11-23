package co.kluvaka.cmp.features.trophies.ui.detail

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
import co.kluvaka.cmp.features.common.domain.DateFormatter
import co.kluvaka.cmp.features.photos.ui.DetailedPhotoViewScreen
import co.kluvaka.cmp.features.trophies.ui.add.trophy.AddTrophyScreen
import coil3.compose.rememberAsyncImagePainter
import org.koin.compose.viewmodel.koinViewModel

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TrophyDetailScreen(
  private val trophyId: Int,
) : Screen {

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<TrophyDetailViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(trophyId) {
      viewModel.loadTrophy(trophyId)
    }

    Scaffold(
      topBar = {
        TopAppBar(
          windowInsets = WindowInsets(0, 0, 0, 0),
          title = { Text(state.trophy?.fishType ?: "Детали трофея") },
          navigationIcon = {
            IconButton(
              onClick = {
                navigator?.pop()
              },
            ) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
          },
          actions = {
            IconButton(
              onClick = {
                navigator?.push(AddTrophyScreen(state.trophy))
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
          .padding(horizontal = 16.dp)
          .verticalScroll(rememberScrollState()),
      ) {
        state.trophy?.let { trophy ->
          Spacer(modifier = Modifier.height(16.dp))
          Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            trophy.weight?.let { weight ->
              Text(
                text = "Вес: $weight кг",
                style = MaterialTheme.typography.bodyMedium
              )
            }
            trophy.length?.let { length ->
              Text(
                text = "Длина: $length см",
                style = MaterialTheme.typography.bodyMedium
              )
            }
            trophy.location?.let { location ->
              Text(
                text = "Место: $location",
                style = MaterialTheme.typography.bodyMedium
              )
            }

            trophy.date?.let { date ->
              Text(
                text = "Дата: ${DateFormatter.format(date)}",
                style = MaterialTheme.typography.bodyMedium
              )
            }

            if (trophy.images.isNotEmpty()) {
              Spacer(modifier = Modifier.padding(8.dp))
              Text(
                text = "Фото трофея:",
                style = MaterialTheme.typography.titleMedium
              )
              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                items(trophy.images.size) { index ->
                  val image = trophy.images[index]
                  Box(
                    modifier = Modifier
                      .height(200.dp)
                      .width(300.dp) // Fixed width for carousel items
                  ) {
                    Card(
                      onClick = {
                        navigator?.push(
                          DetailedPhotoViewScreen(
                            images = trophy.images,
                            initialIndex = index,
                          )
                        )
                      },
                      modifier = Modifier.fillMaxSize(),
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Image(
                        painter = rememberAsyncImagePainter(image),
                        contentDescription = "Trophy photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                      )
                    }
                  }
                }
              }
            }

            trophy.notes?.let { notes ->
              Spacer(modifier = Modifier.padding(8.dp))
              Text(
                text = "Заметки:",
                style = MaterialTheme.typography.titleMedium
              )
              Text(
                text = notes,
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}
