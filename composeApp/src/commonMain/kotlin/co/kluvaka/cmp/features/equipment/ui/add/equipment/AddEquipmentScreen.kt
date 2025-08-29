package co.kluvaka.cmp.features.equipment.ui.add.equipment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import co.kluvaka.cmp.features.equipment.ui.add.equipment.composable.AddEquipmentTopBar
import org.koin.compose.viewmodel.koinViewModel

object AddEquipmentScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current
    val viewModel = koinViewModel<AddEquipmentViewModel>()
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .imePadding() // adds bottom padding when keyboard is shown
        .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    )  {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        AddEquipmentTopBar { navigator?.pop() }
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Name") },
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        )
        OutlinedTextField(
          value = price,
          onValueChange = { price = it },
          label = { Text("Price") },
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        )
      }

      Box {
        Button(
          onClick = {
            if (title.isNotBlank() && price.isNotBlank()) {
              viewModel.addEquipment(
                title = title,
                price = price.toDouble(),
              )
              navigator?.pop()
            }
          },
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
        ) {
          Text("Save")
        }
      }
    }
  }
}
