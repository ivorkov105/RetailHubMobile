package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.domain.models.shop.DepartmentModel
import studying.diplom.retailhub.domain.models.user.UserModel
import studying.diplom.retailhub.data.entities.user.UserEntity as ApiUserEntity
import studying.diplom.retailhub.database.UserEntity as DbUserEntity

fun ApiUserEntity.toModel() = UserModel(
	id = id,
	storeId = storeId,
	phoneNumber = phoneNumber,
	firstName = firstName,
	lastName = lastName,
	role = role,
	currentStatus = currentStatus,
	departments = departments.map { it.toModel() },
	password = password,
	createdAt = createdAt
)

fun UserModel.toApiEntity() = ApiUserEntity(
	id = id,
	storeId = storeId,
	phoneNumber = phoneNumber,
	firstName = firstName,
	lastName = lastName,
	role = role,
	currentStatus = currentStatus,
	departments = departments.map { it.toApiEntity() },
	password = password,
	createdAt = createdAt
)

fun ApiUserEntity.toDbEntity() = DbUserEntity(
	id = id,
	storeId = storeId,
	phoneNumber = phoneNumber,
	firstName = firstName,
	lastName = lastName,
	role = role,
	currentStatus = currentStatus,
	departmentIds = departments.map { it.id }.joinToString(","),
	createdAt = createdAt
)

fun DbUserEntity.toModel(allDepartments: List<DepartmentModel> = emptyList()): UserModel {
	val ids = if (departmentIds.isEmpty()) emptyList() else departmentIds.split(",")
	return UserModel(
		id = id,
		storeId = storeId,
		phoneNumber = phoneNumber,
		firstName = firstName,
		lastName = lastName,
		role = role,
		currentStatus = currentStatus,
		departments = allDepartments.filter { it.id in ids },
		createdAt = createdAt
	)
}

fun UserModel.toDbEntity() = DbUserEntity(
	id = id,
	storeId = storeId,
	phoneNumber = phoneNumber,
	firstName = firstName,
	lastName = lastName,
	role = role,
	currentStatus = currentStatus,
	departmentIds = departments.joinToString(",") { it.id },
	createdAt = createdAt
)
