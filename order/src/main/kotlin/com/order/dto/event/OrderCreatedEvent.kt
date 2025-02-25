package com.order.dto.event

data class OrderCreatedEvent(
    val orderId: Long,
    val customerName: String,
    val items: List<OrderItemCreated>
)
