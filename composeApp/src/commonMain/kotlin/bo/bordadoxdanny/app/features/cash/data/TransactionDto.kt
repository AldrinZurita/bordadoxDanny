package bo.bordadoxdanny.app.features.cash.data

import kotlinx.serialization.Serializable
import bo.bordadoxdanny.app.core.domain.SyncStatus

@Serializable
data class TransactionDto(
    val id: Long? = null,
    val amount: Double = 0.0,
    val type: String = "",
    val description: String = "",
    val reference: String = "",
    val category: String? = null,
    val timestamp: Long = 0L
)

fun TransactionEntity.toDto() = TransactionDto(
    id = id,
    amount = amount,
    type = type,
    description = description,
    reference = reference,
    category = category,
    timestamp = timestamp
)

fun TransactionDto.toEntity(id: Long? = null) = TransactionEntity(
    id = id ?: this.id ?: 0L,
    amount = amount,
    type = type,
    description = description,
    reference = reference,
    category = category,
    timestamp = timestamp,
    syncStatus = SyncStatus.SYNCED
)
