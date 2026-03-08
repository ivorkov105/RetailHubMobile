package studying.diplom.retailhub.domain.use_cases.shift_use_cases

import studying.diplom.retailhub.data.enteties.shift.ShiftEntity
import studying.diplom.retailhub.domain.repositories.ShiftRepository

class StartShiftUseCase(
    private val shiftRepository: ShiftRepository
) {
    suspend operator fun invoke(): Result<ShiftEntity> = shiftRepository.startShift()
}
