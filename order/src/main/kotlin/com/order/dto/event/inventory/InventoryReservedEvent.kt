package com.order.dto.event.inventory

data class InventoryReservedEvent(
    override val orderId: Long,
    val reservedItems: List<ReservedItem>
) : InventoryEvent()
