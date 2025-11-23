package co.kluvaka.cmp.features.photos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage

data class DetailedPhotoViewScreen(
    val images: List<String>,
    val initialIndex: Int = 0
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val pagerState = rememberPagerState(initialPage = initialIndex) { images.size }

      Scaffold(
        topBar = {
          TopAppBar(
            title = {},
            navigationIcon = {
              IconButton(onClick = { navigator?.pop() }) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
                  tint = Color.Companion.White
                )
              }
            },
            colors = TopAppBarDefaults.topAppBarColors(
              containerColor = Color.Companion.Transparent
            )
          )
        },
        containerColor = Color.Companion.Black
      ) { paddingValues ->
        Box(
          modifier = Modifier.Companion
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.Companion.Black),
          contentAlignment = Alignment.Companion.Center
        ) {
          HorizontalPager(
            state = pagerState,
            modifier = Modifier.Companion.fillMaxSize()
          ) { page ->
            Box(
              modifier = Modifier.Companion.fillMaxSize(),
              contentAlignment = Alignment.Companion.Center
            ) {
              AsyncImage(
                model = images[page],
                contentDescription = "Full screen photo",
                modifier = Modifier.Companion.fillMaxWidth(),
                contentScale = ContentScale.Companion.Fit
              )
            }
          }
        }
      }
    }
}