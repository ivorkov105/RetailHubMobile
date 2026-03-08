package studying.diplom.retailhub.domain.use_cases.store_use_cases

import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.repositories.StoreRepository

class GetDepartmentUseCase(private val repository: StoreRepository) {
	suspend operator fun invoke(departmentId: String): Result<DepartmentModel> = repository.getDepartment(departmentId)
}
