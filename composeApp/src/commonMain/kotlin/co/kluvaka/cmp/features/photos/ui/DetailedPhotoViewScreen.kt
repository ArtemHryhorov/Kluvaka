package co.kluvaka.cmp.features.photos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import kluvaka.composeapp.generated.resources.Res
import kluvaka.composeapp.generated.resources.back
import kluvaka.composeapp.generated.resources.full_screen_photo_content_description
import org.jetbrains.compose.resources.stringResource

data class DetailedPhotoViewScreen(
    val images: List<String>,
    val initialIndex: Int = 0
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val pagerState = rememberPagerState(initialPage = initialIndex) { images.size }

      Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
          contentAlignment = Alignment.Center
        ) {
          HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
          ) { page ->
            Box(
              modifier = Modifier.fillMaxSize(),
              contentAlignment = Alignment.Center
            ) {
              AsyncImage(
                model = images[page],
                contentDescription = stringResource(Res.string.full_screen_photo_content_description),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit
              )
            }
          }
          Box(
            modifier = Modifier
              .padding(horizontal = 24.dp)
              .size(48.dp)
              .background(
                color = MaterialTheme.colorScheme.scrim,
                shape = CircleShape,
              )
              .align(Alignment.TopEnd),
          ) {
            IconButton(
              modifier = Modifier.align(Alignment.Center),
              onClick = { navigator?.pop() },
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(Res.string.back),
                tint = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }
}