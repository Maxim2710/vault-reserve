package com.inventory.dto

data class InventoryCreateDto(
    val productId: Long,
    val productName: String,
    val availableStock: Int,
    val reservedStock: Int? = 0
)
