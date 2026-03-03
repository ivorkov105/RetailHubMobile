package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.user.DepartmentEntity
import studying.diplom.retailhub.data.enteties.user.UserEntity
import studying.diplom.retailhub.domain.models.DepartmentModel
import studying.diplom.retailhub.domain.models.UserModel

fun UserEntity.toModel(): UserModel = UserModel(
    id = id,
    storeId = storeId,
    phoneNumber = phoneNumber,
    firstName = firstName,
    lastName = lastName,
    role = role,
    currentStatus = currentStatus,
    departments = departments.map { it.toModel() },
    createdAt = createdAt
)

fun DepartmentEntity.toModel(): DepartmentModel = DepartmentModel(
    id = id,
    name = name
)
