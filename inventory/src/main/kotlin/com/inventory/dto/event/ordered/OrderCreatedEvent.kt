package com.inventory.dto.event.ordered

data class OrderCreatedEvent(
    val orderId: Long,
    val customerName: String,
    val items: List<OrderItemCreated>
)
