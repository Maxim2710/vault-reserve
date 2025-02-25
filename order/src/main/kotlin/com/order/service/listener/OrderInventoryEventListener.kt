package com.order.service.listener

import com.order.dto.event.inventory.InventoryEvent
import com.order.dto.event.inventory.InventoryRejectedEvent
import com.order.dto.event.inventory.InventoryReservedEvent
import com.order.model.enums.OrderStatus
import com.order.service.OrderService
import jakarta.transaction.Transactional
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class OrderInventoryEventListener(
    private val orderService: OrderService
) {

    @KafkaListener(
        topics = ["inventory-events"],
        groupId = "order-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    fun handleInventoryEvent(event: InventoryEvent) {
        when (event) {
            is InventoryReservedEvent -> {
                orderService.updateOrderStatus(event.orderId, OrderStatus.CONFIRMED)
            }
            is InventoryRejectedEvent -> {
                orderService.updateOrderStatus(event.orderId, OrderStatus.REJECTED)
            }
        }
    }
}