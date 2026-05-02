package org.bastanchu.churiservicesv2.articles.domain

interface DatabaseHealthChecker {
    fun check(): DatabaseHealthInfo
}

data class DatabaseHealthInfo(
    val running: Boolean,
    val componentName: String,
    val version: String
)
