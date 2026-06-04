package bo.bordadoxdanny.app.features.orders.data

import bo.bordadoxdanny.app.features.orders.domain.Order

fun OrderEntity.toDomain() = Order(
    id = id,
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
