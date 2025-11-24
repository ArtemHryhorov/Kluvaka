package co.kluvaka.cmp.features.common.ui

sealed interface DialogState<out T> {
  data class Shown<T>(val value: T) : DialogState<T>
  data object Hidden : DialogState<Nothing>
}

