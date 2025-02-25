package com.inventory.model

import jakarta.persistence.*

@Entity
@Table(name = "inventory")
data class Inventory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "product_id", nullable = false, unique = true)
    val productId: Long,

    @Column(name = "product_name", nullable = false)
    var productName: String,

    @Column(name = "available_stock", nullable = false)
    var availableStock: Int,

    @Column(name = "reserved_stock", nullable = false)
    var reservedStock: Int = 0
)
