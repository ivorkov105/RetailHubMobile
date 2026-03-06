package studying.diplom.retailhub.domain.use_cases.shop_use_cases

import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class DeleteDepartmentUseCase(private val repository: StoreRepository) {
    suspend operator fun invoke(department: DepartmentModel): Result<Unit> = repository.deleteDepartment(department)
}
