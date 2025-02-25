package com.inventory.service

import com.inventory.dto.InventoryCreateDto
import com.inventory.dto.InventoryResponseDto
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
}