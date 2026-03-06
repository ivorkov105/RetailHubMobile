package studying.diplom.retailhub.domain.models.user

import studying.diplom.retailhub.domain.models.shop.DepartmentModel

data class UserModel(
	val id: String,
	val storeId: String,
	val phoneNumber: String,
	val firstName: String,
	val lastName: String,
	val role: String,
	val currentStatus: String,
	val departments: List<DepartmentModel>,
	val createdAt: String
)