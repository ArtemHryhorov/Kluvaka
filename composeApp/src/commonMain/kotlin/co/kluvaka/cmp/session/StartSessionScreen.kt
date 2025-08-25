@file:OptIn(ExperimentalMaterial3Api::class)

package co.kluvaka.cmp.session

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.ui.tooling.preview.Preview

object StartSessionScreen : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.current

    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Text(
              text = "Начало сессии",
            )
          },
          navigationIcon = {
            IconButton(
              onClick = { navigator?.pop() },
              content = {
//                Icon(
//                  imageVector = Icons.Default.ArrowBack,
//                  contentDescription = "Back"
//              )
              }
            )
          }
        )
      },
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        var sessionName by rememberSaveable { mutableStateOf("Рыбалка число") }
        TextField(
          value = sessionName,
          onValueChange = { sessionName = it },
        )
        Text(
          text = "Локация?",
        )
        Text(
          text = "Колличество удочек",
        )
        var rodCount by rememberSaveable { mutableStateOf(0) }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          for (index in 1..4) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(
                  color = Color.Blue,
                  shape = RoundedCornerShape(size = 12.dp),
                )
                .clickable {
                  rodCount = index
                }
            ) {
              Text(
                text = index.toString(),
              )
            }
          }
        }
        LazyColumn {
          items(count = rodCount) {
            Card {
              Text(
                text = "Название",
              )
              Text(
                text = "Монтаж",
              )
              Text(
                text = "Насадка",
              )
              Text(
                text = "Дистанция",
              )
              Text(
                text = "Таймер перезаброса",
              )
            }
          }
        }
        var groundbait by rememberSaveable { mutableStateOf("Пелец, кукурудза") }
        TextField(
          value = groundbait,
          onValueChange = { groundbait = it },
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = { }
        ) {
          Text(
            text = "Начать",
          )
        }
      }
    }
  }
}

@Composable
@Preview
internal fun PreviewStartSessionScreen() {
  StartSessionScreen.Content()
}