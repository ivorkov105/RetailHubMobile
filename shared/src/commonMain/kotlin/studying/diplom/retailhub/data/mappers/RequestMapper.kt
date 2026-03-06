package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.request.RequestEntity as ApiRequestEntity
import studying.diplom.retailhub.database.RequestEntity as DbRequestEntity
import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.data.enteties.request.RequestStatus

fun ApiRequestEntity.toModel() = RequestModel(
    id = id,
    clientSessionToken = clientSessionToken,
    status = status,
    departmentName = departmentName,
    consultantName = consultantName,
    canRemind = canRemind,
    canReassign = canReassign,
    createdAt = createdAt,
    assignedAt = assignedAt
)

fun RequestModel.toApiEntity() = ApiRequestEntity(
    id = id,
    clientSessionToken = clientSessionToken,
    status = status,
    departmentName = departmentName,
    consultantName = consultantName,
    canRemind = canRemind,
    canReassign = canReassign,
    createdAt = createdAt,
    assignedAt = assignedAt
)

fun ApiRequestEntity.toDbEntity() = DbRequestEntity(
    id = id,
    clientSessionToken = clientSessionToken,
    status = status.name,
    departmentName = departmentName,
    consultantName = consultantName,
    canRemind = canRemind,
    canReassign = canReassign,
    createdAt = createdAt,
    assignedAt = assignedAt
)

fun DbRequestEntity.toApiEntity() = ApiRequestEntity(
    id = id,
    clientSessionToken = clientSessionToken,
    status = RequestStatus.valueOf(status),
    departmentName = departmentName,
    consultantName = consultantName,
    canRemind = canRemind,
    canReassign = canReassign,
    createdAt = createdAt,
    assignedAt = assignedAt
)

fun DbRequestEntity.toModel() = RequestModel(
    id = id,
    clientSessionToken = clientSessionToken,
    status = RequestStatus.valueOf(status),
    departmentName = departmentName,
    consultantName = consultantName,
    canRemind = canRemind,
    canReassign = canReassign,
    createdAt = createdAt,
    assignedAt = assignedAt
)

fun RequestModel.toDbEntity() = DbRequestEntity(
    id = id,
    clientSessionToken = clientSessionToken,
    status = status.name,
    departmentName = departmentName,
    consultantName = consultantName,
    canRemind = canRemind,
    canReassign = canReassign,
    createdAt = createdAt,
    assignedAt = assignedAt
)
