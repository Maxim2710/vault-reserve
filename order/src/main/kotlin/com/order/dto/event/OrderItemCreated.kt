package com.order.dto.event

data class OrderItemCreated(
    val productId: Long,
    val quantity: Int
)
