package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.entities.qr_codes.QREntity
import studying.diplom.retailhub.domain.models.qr_code.QrCodeModel
import studying.diplom.retailhub.database.QrCodeEntity as DbQREntity

fun QREntity.toDbEntity(): DbQREntity {
    return DbQREntity(
        id = id,
        departmentId = departmentId,
        departmentName = departmentName,
        token = token,
        scanUrl = scanUrl,
        label = label,
        isActive = isActive,
        createdAt = createdAt
    )
}

fun DbQREntity.toApiEntity(): QREntity {
    return QREntity(
        id = id,
        departmentId = departmentId,
        departmentName = departmentName,
        token = token,
        scanUrl = scanUrl,
        label = label,
        isActive = isActive,
        createdAt = createdAt
    )
}

fun QREntity.toModel(): QrCodeModel {
    return QrCodeModel(
        id = id,
        departmentId = departmentId,
        departmentName = departmentName,
        token = token,
        scanUrl = scanUrl,
        label = label,
        isActive = isActive,
        createdAt = createdAt
    )
}

fun DbQREntity.toModel(): QrCodeModel {
    return QrCodeModel(
        id = id,
        departmentId = departmentId,
        departmentName = departmentName,
        token = token,
        scanUrl = scanUrl,
        label = label,
        isActive = isActive,
        createdAt = createdAt
    )
}
