package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.entities.shop.DepartmentEntity as ApiDepartmentEntity
import studying.diplom.retailhub.database.DepartmentEntity as DbDepartmentEntity
import studying.diplom.retailhub.domain.models.shop.DepartmentModel

fun ApiDepartmentEntity.toModel() = DepartmentModel(
    id = id,
    storeId = storeId,
    name = name,
    description = description,
    createdAt = createdAt
)

fun DepartmentModel.toApiEntity() = ApiDepartmentEntity(
    id = id,
    storeId = storeId,
    name = name,
    description = description,
    createdAt = createdAt
)

fun ApiDepartmentEntity.toDbEntity() = DbDepartmentEntity(
    id = id,
    storeId = storeId,
    name = name,
    description = description,
    createdAt = createdAt
)

fun DbDepartmentEntity.toApiEntity() = ApiDepartmentEntity(
    id = id,
    storeId = storeId,
    name = name,
    description = description,
    createdAt = createdAt
)

fun DbDepartmentEntity.toModel() = DepartmentModel(
    id = id,
    storeId = storeId,
    name = name,
    description = description,
    createdAt = createdAt
)

fun DepartmentModel.toDbEntity() = DbDepartmentEntity(
    id = id,
    storeId = storeId,
    name = name,
    description = description,
    createdAt = createdAt
)
