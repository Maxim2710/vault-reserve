package com.inventory.service

import com.inventory.dto.InventoryCreateDto
import com.inventory.dto.InventoryResponseDto
import com.inventory.dto.event.ordered.OrderCreatedEvent
import com.inventory.model.Inventory
import com.inventory.repository.InventoryRepository
import org.springframework.stereotype.Service

@Service
class InventoryService(
    private val inventoryRepository: InventoryRepository
) {

    fun createInventory(createDto: InventoryCreateDto): InventoryResponseDto {
        val reserved = createDto.reservedStock ?: 0

        if (reserved < createDto.availableStock) {
            throw IllegalArgumentException("Зарезервированное количество не может превышать доступное количество")
        }

        val inventory = Inventory(
            productId = createDto.productId,
            productName = createDto.productName,
            availableStock = createDto.availableStock,
            reservedStock = reserved
        )

        val savedInformation = inventoryRepository.save(inventory)

        return savedInformation.toDto()
    }

    private fun Inventory.toDto() = InventoryResponseDto(
        id = this.id,
        productId = this.productId,
        productName = this.productName,
        availableStock = this.availableStock,
        reservedStock = this.reservedStock
    )

    fun getInventoryById(id: Long): InventoryResponseDto {
        val inventory = inventoryRepository.findById(id)
            .orElseThrow { throw IllegalArgumentException("User not found with id: $id")}

        return InventoryResponseDto(
            id = inventory.id,
            productId = inventory.productId,
            productName = inventory.productName,
            availableStock = inventory.availableStock,
            reservedStock = inventory.reservedStock
        )
    }

    fun reserveStockForOrder(orderEvent: OrderCreatedEvent): Boolean {
        for (item in orderEvent.items) {
            val inventoryOpt = inventoryRepository.findByProductId(item.productId)
            if (inventoryOpt.isEmpty) return false

            val inventory = inventoryOpt.get()
            if (inventory.availableStock < item.quantity) return false
        }

        orderEvent.items.forEach { item ->
            val inventory = inventoryRepository.findByProductId(item.productId).get()
            inventory.availableStock -= item.quantity
            inventory.reservedStock += item.quantity
            inventoryRepository.save(inventory)
        }

        return true
    }

    fun getAllInventories(): List<InventoryResponseDto> {
        return inventoryRepository.findAll().map { inventory ->
            InventoryResponseDto(
                id = inventory.id,
                productId = inventory.productId,
                productName = inventory.productName,
                availableStock = inventory.availableStock,
                reservedStock = inventory.reservedStock
            )
        }
    }
}