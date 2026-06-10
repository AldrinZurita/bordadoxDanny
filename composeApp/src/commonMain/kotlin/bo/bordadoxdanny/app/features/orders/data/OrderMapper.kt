package bo.bordadoxdanny.app.features.orders.data

import bo.bordadoxdanny.app.features.orders.domain.Order

fun OrderEntity.toDomain() = Order(
    id = id,
    userId = userId,
    customerName = customerName,
    deliveryDate = deliveryDate,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    initialPayment = initialPayment,
    total = total,
    balance = balance,
    status = status,
    syncStatus = syncStatus,
    createdAt = createdAt
)

fun Order.toEntity() = OrderEntity(
    id = id,
    userId = userId,
    customerName = customerName,
    deliveryDate = deliveryDate,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    initialPayment = initialPayment,
    total = total,
    balance = balance,
    status = status,
    syncStatus = syncStatus,
    createdAt = createdAt
)

fun OrderDto.toEntity() = OrderEntity(
    id = id.toLongOrNull() ?: 0,
    userId = userId,
    customerName = customerName,
    deliveryDate = deliveryDate,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    initialPayment = initialPayment,
    total = total,
    balance = balance,
    status = status,
    syncStatus = syncStatus,
    createdAt = createdAt
)

fun OrderEntity.toDto() = OrderDto(
    id = id.takeIf { it > 0 }?.toString() ?: "",
    userId = userId,
    customerName = customerName,
    deliveryDate = deliveryDate,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    initialPayment = initialPayment,
    total = total,
    balance = balance,
    status = status,
    syncStatus = syncStatus,
    createdAt = createdAt
)

fun Order.toDto() = OrderDto(
    id = id.takeIf { it > 0 }?.toString() ?: "",
    userId = userId,
    customerName = customerName,
    deliveryDate = deliveryDate,
    description = description,
    quantity = quantity,
    unitPrice = unitPrice,
    initialPayment = initialPayment,
    total = total,
    balance = balance,
    status = status,
    syncStatus = syncStatus,
    createdAt = createdAt
)
