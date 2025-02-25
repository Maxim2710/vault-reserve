package com.order.service

import com.order.dto.OrderCreateDto
import com.order.dto.OrderResponseDto
import com.order.dto.event.OrderCreatedEvent
import com.order.dto.event.OrderItemCreated
import com.order.model.OrderItem
import com.order.model.Order
import com.order.model.enums.OrderStatus
import com.order.repository.OrderRepository
import jakarta.transaction.Transactional
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    @Transactional
    fun createOrder(createDto: OrderCreateDto): OrderResponseDto {
        val order = Order (
            customerName = createDto.customerName,
            status = OrderStatus.PENDING,
            createdAt = LocalDateTime.now(),
            items = mutableListOf()
        )

        val orderItems = createDto.items.map {itemDto ->
            OrderItem (
                productId = itemDto.productId,
                quantity = itemDto.quantity,
                order = order
            )
        }

        (order.items as MutableList).addAll(orderItems)

        val savedOrder = orderRepository.save(order)

        kafkaTemplate.executeInTransaction { template ->
            val event = OrderCreatedEvent(
                orderId = savedOrder.id,
                customerName = savedOrder.customerName,
                items = savedOrder.items.map { orderItem ->
                    OrderItemCreated(
                        productId = orderItem.productId,
                        quantity = orderItem.quantity
                    )
                }
            )

            val key = savedOrder.items.first().productId.toString()
            template.send("order-events", key, event)
        }

        return OrderResponseDto(
            id = savedOrder.id,
            customerName = savedOrder.customerName,
            status = savedOrder.status.name,
            createdAt = savedOrder.createdAt.toString()
        )
    }
}