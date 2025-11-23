package co.kluvaka.cmp.features.trophies.ui.trophies

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

data class TrophiesState(
  val trophies: List<Trophy> = emptyList(),
  val deleteConfirmationDialog: DialogState<Trophy> = DialogState.Hidden,
)

sealed interface DialogState<out T> {
  data class Shown<T>(val value: T) : DialogState<T>
  data object Hidden : DialogState<Nothing>
}