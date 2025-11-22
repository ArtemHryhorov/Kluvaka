package co.kluvaka.cmp.features.trophies.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.koin.compose.viewmodel.koinViewModel
import coil3.compose.rememberAsyncImagePainter

class TrophyDetailScreen(private val trophyId: Int) : Screen {
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
          title = { Text("Детали трофея") },
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
          .padding(paddingValues)
          .padding(16.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        state.trophy?.let { trophy ->
          Card(
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = trophy.fishType,
                style = MaterialTheme.typography.headlineMedium
              )
              
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Вес: ${trophy.weight} кг",
                  style = MaterialTheme.typography.bodyLarge
                )
                trophy.length?.let { length ->
                  Text(
                    text = "Длина: $length см",
                    style = MaterialTheme.typography.bodyLarge
                  )
                }
              }

              Text(
                text = "Место: ${trophy.location}",
                style = MaterialTheme.typography.bodyMedium
              )

              Text(
                text = "Дата: ${trophy.date}",
                style = MaterialTheme.typography.bodyMedium
              )

              trophy.image?.let { image ->
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                  text = "Фото трофея:",
                  style = MaterialTheme.typography.titleMedium
                )
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                ) {
                  Box(
                    modifier = Modifier.fillMaxSize(),
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
          }
        } ?: run {
          Text(
            text = "Трофей не найден",
            style = MaterialTheme.typography.bodyLarge
          )
        }
      }
    }
  }
}
