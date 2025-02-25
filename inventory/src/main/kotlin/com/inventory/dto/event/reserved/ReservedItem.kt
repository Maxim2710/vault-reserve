package com.inventory.dto.event.reserved

data class ReservedItem(
    val productId: Long,
    val reservedQuantity: Int
)
