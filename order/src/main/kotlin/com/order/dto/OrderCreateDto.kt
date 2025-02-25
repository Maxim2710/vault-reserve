package com.order.dto

data class OrderCreateDto(
    val customerName: String,
    val items: List<OrderItemCreateDto>
)
