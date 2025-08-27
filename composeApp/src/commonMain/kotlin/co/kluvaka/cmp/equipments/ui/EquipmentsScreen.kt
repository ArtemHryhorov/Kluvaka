package co.kluvaka.cmp.equipments.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.equipments.domain.Equipment
import org.koin.compose.viewmodel.koinViewModel

object EquipmentsScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<EquipmentsViewModel>()
    val state by viewModel.state.collectAsState()

    Scaffold(
      topBar = {
        EquipmentsTopBar(state.totalPrice)
      }
    ) { padding ->
      LazyColumn(
        contentPadding = padding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        items(state.equipments.size) { index ->
          val item = state.equipments[index]
          EquipmentCard(item)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EquipmentsTopBar(totalPrice: Double) {
  TopAppBar(
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
private fun EquipmentCard(
  item: Equipment,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(text = item.title, style = MaterialTheme.typography.titleMedium)
      Text(text = "₴${item.price}", style = MaterialTheme.typography.bodyMedium)
    }
  }
}
