package studying.diplom.retailhub.domain.use_cases.shop_use_cases

import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class UpdateDepartmentUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(department: DepartmentModel): Result<DepartmentModel> = repository.updateDepartment(department)
}
