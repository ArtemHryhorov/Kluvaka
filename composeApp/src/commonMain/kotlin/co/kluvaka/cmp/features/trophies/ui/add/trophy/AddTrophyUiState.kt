package co.kluvaka.cmp.features.trophies.ui.add.trophy

import co.kluvaka.cmp.features.trophies.domain.model.Trophy

data class AddTrophyUiState(
  val trophyInput: TrophyInput = TrophyInput(),
  val mode: AddTrophyMode = AddTrophyMode.New,
)

data class TrophyInput(
  val fishType: String = "",
  val weight: String = "",
  val length: String = "",
  val location: String = "",
  val date: Long? = null,
  val images: List<String> = emptyList(),
  val notes: String = "",
)

sealed interface AddTrophyMode {
  object New : AddTrophyMode
  data class Edit(val trophy: Trophy) : AddTrophyMode
}
