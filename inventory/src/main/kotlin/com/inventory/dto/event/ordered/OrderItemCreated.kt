package com.inventory.dto.event.ordered

data class OrderItemCreated(
    val productId: Long,
    val quantity: Int
)
