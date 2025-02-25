package com.inventory.repository

import com.inventory.model.Inventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InventoryRepository: JpaRepository<Inventory, Long> {

}