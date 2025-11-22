package co.kluvaka.cmp.features.equipment.ui.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

object EquipmentDetailsScreen : Screen {
  @Composable
  override fun Content() {
    Box(modifier = Modifier.fillMaxSize()) {
      Text(text = "Equipment Details")
    }
  }
}