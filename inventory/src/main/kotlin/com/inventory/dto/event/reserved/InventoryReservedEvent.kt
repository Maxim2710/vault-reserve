package com.inventory.dto.event.reserved

data class InventoryReservedEvent(
    val orderId: Long,
    val reservedItems: List<ReservedItem>
)
