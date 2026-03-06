package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.shop.StoreEntity as ApiStoreEntity
import studying.diplom.retailhub.database.StoreEntity as DbStoreEntity
import studying.diplom.retailhub.domain.models.shop.StoreModel

fun ApiStoreEntity.toModel() = StoreModel(
    id = id,
    name = name,
    address = address,
    timezone = timezone,
    createdAt = createdAt
)

fun StoreModel.toApiEntity() = ApiStoreEntity(
    id = id,
    name = name,
    address = address,
    timezone = timezone,
    createdAt = createdAt
)

fun ApiStoreEntity.toDbEntity() = DbStoreEntity(
    id = id,
    name = name,
    address = address,
    timezone = timezone,
    createdAt = createdAt
)

fun DbStoreEntity.toApiEntity() = ApiStoreEntity(
    id = id,
    name = name,
    address = address,
    timezone = timezone,
    createdAt = createdAt
)

fun DbStoreEntity.toModel() = StoreModel(
    id = id,
    name = name,
    address = address,
    timezone = timezone,
    createdAt = createdAt
)

fun StoreModel.toDbEntity() = DbStoreEntity(
    id = id,
    name = name,
    address = address,
    timezone = timezone,
    createdAt = createdAt
)
