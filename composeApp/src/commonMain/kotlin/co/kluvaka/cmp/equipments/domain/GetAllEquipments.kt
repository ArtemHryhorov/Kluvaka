package co.kluvaka.cmp.equipments.domain

class GetAllEquipmentsImpl : GetAllEquipments {
  override suspend fun invoke() = listOf(
    Equipment(
      id = 0,
      title = "Удочка Daiwa Black Widow",
      image = null,
      price = 2800.0,
    ),
    Equipment(
      id = 1,
      title = "Катушка Brain Mission SE 8000",
      image = null,
      price = 3200.0,
    ),
    Equipment(
      id = 2,
      title = "Леска Carp Pro 1000 м. 0.28",
      image = null,
      price = 800.0,
    ),
    Equipment(
      id = 3,
      title = "Крючки Fox 8",
      image = null,
      price = 190.0,
    ),
  )
}

fun interface GetAllEquipments {
  suspend operator fun invoke(): List<Equipment>
}