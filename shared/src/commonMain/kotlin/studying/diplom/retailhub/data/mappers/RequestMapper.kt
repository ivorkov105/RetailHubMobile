package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.domain.models.RequestModel

fun RequestEntity.toModel() = RequestModel(
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

fun RequestModel.toEntity() = RequestEntity(
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
