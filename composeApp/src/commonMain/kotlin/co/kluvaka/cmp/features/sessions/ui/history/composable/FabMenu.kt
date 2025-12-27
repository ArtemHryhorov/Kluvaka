package co.kluvaka.cmp.features.sessions.ui.history.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi

@OptIn(
  InternalVoyagerApi::class, ExperimentalMaterial3ExpressiveApi::class,
  ExperimentalMaterial3Api::class
)
@Composable
fun FabMenu(
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
) {
  val focusRequester = FocusRequester()

  val items =
    listOf(
      Icons.AutoMirrored.Filled.Message to "Спиннинг",
      Icons.Filled.People to "Карпфишинг",
      Icons.Filled.Contacts to "Поплавок",
      Icons.Filled.Snooze to "Фидер",
    )

  var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }

  FloatingActionButtonMenu(
    modifier = modifier,
    expanded = fabMenuExpanded,
    button = {
      // A FAB should have a tooltip associated with it.
      TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text("Toggle menu") } },
        state = rememberTooltipState(),
      ) {
        ToggleFloatingActionButton(
          modifier =
            Modifier.semantics {
              traversalIndex = -1f
              stateDescription =
                if (fabMenuExpanded) "Expanded" else "Collapsed"
              contentDescription = "Toggle menu"
            }
              .animateFloatingActionButton(
                visible = true,
                alignment = Alignment.BottomEnd,
              )
              .focusRequester(focusRequester),
          checked = fabMenuExpanded,
          onCheckedChange = { fabMenuExpanded = !fabMenuExpanded },
        ) {
          val imageVector by remember {
            derivedStateOf {
              if (checkedProgress > 0.5f) Icons.Filled.Close else Icons.Filled.Add
            }
          }
          Icon(
            painter = rememberVectorPainter(imageVector),
            contentDescription = null,
            modifier = Modifier.animateIcon({ checkedProgress }),
          )
        }
      }
    },
  ) {
    items.forEachIndexed { i, item ->
      FloatingActionButtonMenuItem(
        onClick = {
          fabMenuExpanded = false
          onClick()
        },
        icon = { Icon(item.first, contentDescription = null) },
        text = { Text(text = item.second) },
      )
    }
  }
}
