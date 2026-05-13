package studying.diplom.retailhub.data.repositories

import studying.diplom.retailhub.data.data_sources.RemoteSource
import studying.diplom.retailhub.data.entities.shift.ShiftEntity
import studying.diplom.retailhub.domain.repositories.ShiftRepository

class ShiftRepositoryImpl(
    private val remoteSource: RemoteSource
) : ShiftRepository {
    override suspend fun startShift(): Result<ShiftEntity> = remoteSource.startShift()
    override suspend fun endShift(): Result<ShiftEntity> = remoteSource.endShift()
    override suspend fun getMyShifts(dateFrom: String, dateTo: String): Result<List<ShiftEntity>> =
        remoteSource.getMyShifts(dateFrom, dateTo)
}
