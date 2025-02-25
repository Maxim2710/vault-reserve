package com.inventory.service.listener

import com.inventory.dto.event.ordered.OrderCreatedEvent
import com.inventory.dto.event.rejected.InventoryRejectedEvent
import com.inventory.dto.event.reserved.InventoryReservedEvent
import com.inventory.dto.event.reserved.ReservedItem
import com.inventory.service.InventoryService
import jakarta.transaction.Transactional
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class InventoryEventListener(
    private val inventoryService: InventoryService,
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    @KafkaListener(
        topics = ["order-events"],
        groupId = "inventory-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    fun handleOrderCreatedEvent(orderEvent: OrderCreatedEvent) {
        val success = inventoryService.reserveStockForOrder(orderEvent)

        kafkaTemplate.executeInTransaction { template ->
            if (success) {
                val reservedEvent = InventoryReservedEvent(
                    orderId = orderEvent.orderId,
                    reservedItems = orderEvent.items.map { item ->
                        ReservedItem(
                            productId = item.productId,
                            reservedQuantity = item.quantity
                        )
                    }
                )

                template.send("inventory-events", orderEvent.orderId.toString(), reservedEvent)
            } else {
                val rejectedEvent = InventoryRejectedEvent(
                    orderId = orderEvent.orderId,
                    reason = "Insufficient stock for one or more items"
                )

                template.send("inventory-events", orderEvent.orderId.toString(), rejectedEvent)
            }
        }
    }
}