package com.inventory.dto.event.rejected

data class InventoryRejectedEvent(
    val orderId: Long,
    val reason: String
)