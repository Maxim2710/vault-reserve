package com.inventory.controller

import com.inventory.dto.InventoryCreateDto
import com.inventory.dto.InventoryResponseDto
import com.inventory.service.InventoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/inventory")
class InventoryController(
    private val inventoryService: InventoryService
) {

    @PostMapping("/create")
    fun createInventory(@RequestBody createDto: InventoryCreateDto): ResponseEntity<InventoryResponseDto> {
        val response = inventoryService.createInventory(createDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getInventoryById(@PathVariable id: Long): ResponseEntity<InventoryResponseDto> {
        val response = inventoryService.getInventoryById(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/all-inventory")
    fun getAllInventories(): ResponseEntity<List<InventoryResponseDto>> {
        val response = inventoryService.getAllInventories()
        return ResponseEntity.ok(response)
    }
}