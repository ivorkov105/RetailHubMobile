package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.user.UserEntity as ApiUserEntity
import studying.diplom.retailhub.database.UserEntity as DbUserEntity
import studying.diplom.retailhub.domain.models.user.UserModel

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

fun DbUserEntity.toModel() = UserModel(
    id = id,
    storeId = storeId,
    phoneNumber = phoneNumber,
    firstName = firstName,
    lastName = lastName,
    role = role,
    currentStatus = currentStatus,
    departments = emptyList(), 
    createdAt = createdAt
)

fun UserModel.toDbEntity() = DbUserEntity(
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
