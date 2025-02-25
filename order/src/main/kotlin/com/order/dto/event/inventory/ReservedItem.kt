package com.order.dto.event.inventory

data class ReservedItem(
    val productId: Long,
    val reservedQuantity: Int
)
