package com.inventory.dto

data class InventoryResponseDto(
    val id: Long,
    val productId: Long,
    val productName: String,
    val availableStock: Int,
    val reservedStock: Int
)
