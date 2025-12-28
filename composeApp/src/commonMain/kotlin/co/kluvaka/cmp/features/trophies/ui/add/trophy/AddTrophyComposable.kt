package co.kluvaka.cmp.features.trophies.ui.add.trophy

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.add_trophy_title
import kluvaka.composeapp.generated.resources.back
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddTrophyScreenTopAppBar(
  onNavigateBackClick: () -> Unit,
) {
  TopAppBar(
    windowInsets = WindowInsets(0, 0, 0, 0),
    title = { Text(stringResource(Res.string.add_trophy_title)) },
    navigationIcon = {
      IconButton(
        onClick = onNavigateBackClick,
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = stringResource(Res.string.back),
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(),
  )
}
