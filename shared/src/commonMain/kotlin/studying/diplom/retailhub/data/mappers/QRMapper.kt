package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.qr_codes.QREntity
import studying.diplom.retailhub.database.QrCodeEntity as DbQREntity

fun QREntity.toDbEntity(): DbQREntity {
    return DbQREntity(
        id = id,
        storeId = storeId,
        departmentId = departmentId,
        label = label,
        url = url,
        createdAt = createdAt
    )
}

fun DbQREntity.toApiEntity(): QREntity {
    return QREntity(
        id = id,
        storeId = storeId,
        departmentId = departmentId,
        label = label,
        url = url,
        createdAt = createdAt
    )
}
