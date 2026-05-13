package studying.diplom.retailhub.domain.use_cases.shift_use_cases

import studying.diplom.retailhub.data.entities.shift.ShiftEntity
import studying.diplom.retailhub.domain.repositories.ShiftRepository

class GetMyShiftsUseCase(
    private val repository: ShiftRepository
) {
    suspend operator fun invoke(dateFrom: String, dateTo: String): Result<List<ShiftEntity>> {
        return repository.getMyShifts(dateFrom, dateTo)
    }
}
