package studying.diplom.retailhub.domain.repositories

import studying.diplom.retailhub.data.entities.shift.ShiftEntity

interface ShiftRepository {
    suspend fun startShift(): Result<ShiftEntity>
    suspend fun endShift(): Result<ShiftEntity>
    suspend fun getMyShifts(dateFrom: String, dateTo: String): Result<List<ShiftEntity>>
}
