package studying.diplom.retailhub.domain.repositories

import studying.diplom.retailhub.data.enteties.shift.ShiftEntity

interface ShiftRepository {
    suspend fun startShift(): Result<ShiftEntity>
    suspend fun endShift(): Result<ShiftEntity>
}
