package bo.bordadoxdanny.app.features.cash.data

import bo.bordadoxdanny.app.features.cash.domain.Transaction
import bo.bordadoxdanny.app.features.cash.domain.TransactionType

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    amount = amount,
    type = TransactionType.valueOf(type),
    description = description,
    reference = reference,
    timestamp = timestamp,
    syncStatus = syncStatus
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    amount = amount,
    type = type.name,
    description = description,
    reference = reference,
    timestamp = timestamp,
    syncStatus = syncStatus
)
