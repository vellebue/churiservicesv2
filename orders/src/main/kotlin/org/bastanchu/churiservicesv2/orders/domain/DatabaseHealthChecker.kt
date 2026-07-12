package org.bastanchu.churiservicesv2.orders.domain

interface DatabaseHealthChecker {
    fun check(): DatabaseHealthInfo
}

data class DatabaseHealthInfo(
    val running: Boolean,
    val componentName: String,
    val version: String
)
