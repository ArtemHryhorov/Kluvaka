package co.kluvaka.cmp.features.sessions.ui.history.composable

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex

@OptIn(
  ExperimentalMaterial3ExpressiveApi::class,
  ExperimentalMaterial3Api::class
)
@Composable
actual fun FabMenu(
  modifier: Modifier,
  onClick: () -> Unit,
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

  BackHandler(fabMenuExpanded) { fabMenuExpanded = false }

  FloatingActionButtonMenu(
    modifier = modifier,
    expanded = fabMenuExpanded,
    button = {
      TooltipBox(
        positionProvider =
          TooltipDefaults.rememberTooltipPositionProvider(
            if (fabMenuExpanded) {
              androidx.compose.material3.TooltipAnchorPosition.Start
            } else {
              androidx.compose.material3.TooltipAnchorPosition.Above
            }
          ),
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
              if (fabMenuExpanded) Icons.Filled.Close else Icons.Filled.Add
            }
          }
          Icon(
            painter = rememberVectorPainter(imageVector),
            contentDescription = null,
            modifier = Modifier.animateIcon({ if (fabMenuExpanded) 1f else 0f }),
          )
        }
      }
    },
  ) {
    items.forEachIndexed { i, item ->
      FloatingActionButtonMenuItem(
        modifier =
          Modifier.semantics {
            isTraversalGroup = true
            if (i == items.size - 1) {
              customActions =
                listOf(
                  CustomAccessibilityAction(
                    label = "Close menu",
                    action = {
                      fabMenuExpanded = false
                      true
                    },
                  )
                )
            }
          }
            .then(
              if (i == 0) {
                Modifier.onKeyEvent {
                  if (
                    it.type == KeyEventType.KeyDown &&
                    (it.key == Key.DirectionUp ||
                      (it.isShiftPressed && it.key == Key.Tab))
                  ) {
                    focusRequester.requestFocus()
                    return@onKeyEvent true
                  }
                  return@onKeyEvent false
                }
              } else {
                Modifier
              }
            ),
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

