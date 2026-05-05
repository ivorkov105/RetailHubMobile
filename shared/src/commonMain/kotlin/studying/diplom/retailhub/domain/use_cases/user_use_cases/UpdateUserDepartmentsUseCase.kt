package studying.diplom.retailhub.domain.use_cases.user_use_cases

import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.domain.repositories.UserRepository

class UpdateUserDepartmentsUseCase(
	private val repository: UserRepository
) {

	suspend operator fun invoke(userId: String, departmentIds: List<String>): Result<UserModel> {
		return repository.updateUserDepartments(userId, departmentIds)
	}
}
