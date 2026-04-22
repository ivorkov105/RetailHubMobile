package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.request.AssignedRequestUserEntity
import studying.diplom.retailhub.domain.models.request.RequestModel
import studying.diplom.retailhub.data.enteties.request.RequestEntity as ApiRequestEntity
import studying.diplom.retailhub.database.RequestEntity as DbRequestEntity
import studying.diplom.retailhub.domain.models.request.RequestStatus as DomainRequestStatus

fun ApiRequestEntity.toModel() = RequestModel(
	id = id,
	storeId = storeId,
	departmentId = departmentId,
	departmentName = departmentName,
	isEscalated = isEscalated,
    assignedUserId = assignedUser.id.ifBlank { null },
	assignedUserFirstName = assignedUser.firstName.ifBlank { null },
	assignedUserLastName = assignedUser.lastName.ifBlank { null },
	status = DomainRequestStatus.valueOf(status),
	clientSessionToken = clientSessionToken,
	createdAt = createdAt,
	assignedAt = assignedAt.ifBlank { null },
	completedAt = completedAt.ifBlank { null }
)

fun RequestModel.toApiEntity() = ApiRequestEntity(
	id = id,
	storeId = storeId,
	departmentId = departmentId,
	departmentName = departmentName,
	isEscalated = isEscalated,
	assignedUser = AssignedRequestUserEntity(
        id = assignedUserId ?: "",
		firstName = assignedUserFirstName ?: "",
		lastName = assignedUserLastName ?: ""
	),
	status = status.name,
	clientSessionToken = clientSessionToken,
	createdAt = createdAt,
	assignedAt = assignedAt ?: "",
	completedAt = completedAt ?: ""
)

fun ApiRequestEntity.toDbEntity() = DbRequestEntity(
	id = id,
	storeId = storeId,
	departmentId = departmentId,
	departmentName = departmentName,
	isEscalated = isEscalated,
	assignedUserFirstName = assignedUser.firstName.ifBlank { null },
	assignedUserLastName = assignedUser.lastName.ifBlank { null },
	status = status,
	clientSessionToken = clientSessionToken,
	createdAt = createdAt,
	assignedAt = assignedAt.ifBlank { null },
	completedAt = completedAt.ifBlank { null }
)

fun DbRequestEntity.toModel() = RequestModel(
	id = id,
	storeId = storeId,
	departmentId = departmentId,
	departmentName = departmentName,
	isEscalated = isEscalated,
    assignedUserId = null, // В текущей БД нет поля для ID, при необходимости нужно добавить миграцию
	assignedUserFirstName = assignedUserFirstName,
	assignedUserLastName = assignedUserLastName,
	status = DomainRequestStatus.valueOf(status),
	clientSessionToken = clientSessionToken,
	createdAt = createdAt,
	assignedAt = assignedAt,
	completedAt = completedAt
)

fun RequestModel.toDbEntity() = DbRequestEntity(
	id = id,
	storeId = storeId,
	departmentId = departmentId,
	departmentName = departmentName,
	isEscalated = isEscalated,
	assignedUserFirstName = assignedUserFirstName,
	assignedUserLastName = assignedUserLastName,
	status = status.name,
	clientSessionToken = clientSessionToken,
	createdAt = createdAt,
	assignedAt = assignedAt,
	completedAt = completedAt
)
