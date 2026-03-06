package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.user.UserEntity
import studying.diplom.retailhub.domain.models.user.UserModel

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

// Метод toEntity обычно нужен для преобразования из Model в Entity.
// Если нужно создать копию Entity, лучше использовать .copy()
fun UserModel.toEntity(): UserEntity = UserEntity(
    id = id,
    storeId = storeId,
    phoneNumber = phoneNumber,
    firstName = firstName,
    lastName = lastName,
    role = role,
    currentStatus = currentStatus,
    departments = departments.map { it.toApiEntity() },
    createdAt = createdAt
)
