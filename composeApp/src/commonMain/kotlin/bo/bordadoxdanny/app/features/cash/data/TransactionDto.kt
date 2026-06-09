package bo.bordadoxdanny.app.features.cash.data

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: Long? = null,
    val amount: Double = 0.0,
    val type: String = "",
    val description: String = "",
    val reference: String = "",
    val timestamp: Long = 0L
)

fun TransactionEntity.toDto() = TransactionDto(
    id = id,
    amount = amount,
    type = type,
    description = description,
    reference = reference,
    timestamp = timestamp
)

fun TransactionDto.toEntity(id: Long? = null) = TransactionEntity(
    id = id ?: this.id ?: 0L,
    amount = amount,
    type = type,
    description = description,
    reference = reference,
    timestamp = timestamp,
    syncStatus = bo.bordadoxdanny.app.core.domain.SyncStatus.SYNCED
)
