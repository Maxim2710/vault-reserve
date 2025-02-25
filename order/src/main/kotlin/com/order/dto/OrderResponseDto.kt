package com.order.dto

data class OrderResponseDto(
    val id: Long,
    val customerName: String,
    val status: String,
    val createdAt: String
)
