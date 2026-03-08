package studying.diplom.retailhub.domain.use_cases.store_use_cases

import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class AddDepartmentUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(department: DepartmentModel): Result<Unit> = repository.addDepartment(department)
}
