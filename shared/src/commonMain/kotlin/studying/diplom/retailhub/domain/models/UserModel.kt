package studying.diplom.retailhub.domain.models

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

data class DepartmentModel(
    val id: String,
    val name: String
)
