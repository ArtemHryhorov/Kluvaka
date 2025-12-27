package co.kluvaka.cmp.features.trophies.ui.add.trophy

import co.kluvaka.cmp.features.trophies.domain.model.Trophy
import co.kluvaka.cmp.features.trophies.domain.model.TrophyInput

data class AddTrophyState(
  val input: TrophyInput,
  val mode: AddTrophyMode,
) {
  companion object {
    val Initial = AddTrophyState(
      input = TrophyInput.Initial,
      mode = AddTrophyMode.New,
    )
  }
}

sealed interface AddTrophyMode {
  object New : AddTrophyMode
  data class Edit(val trophy: Trophy) : AddTrophyMode
}
