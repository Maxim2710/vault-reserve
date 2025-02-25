package com.order.dto

data class OrderItemCreateDto(
    val productId: Long,
    val quantity: Int
)
