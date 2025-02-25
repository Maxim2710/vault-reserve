package com.order.controller

import com.order.dto.OrderCreateDto
import com.order.dto.OrderResponseDto
import com.order.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(@RequestBody createDto: OrderCreateDto): ResponseEntity<OrderResponseDto> {
        val response = orderService.createOrder(createDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}