package com.order.dto.event.inventory

sealed class InventoryEvent {
    abstract val orderId: Long
}