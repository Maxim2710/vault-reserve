package com.order.dto.event.inventory

data class InventoryRejectedEvent(
    override val orderId: Long,
    val reason: String
) : InventoryEvent()