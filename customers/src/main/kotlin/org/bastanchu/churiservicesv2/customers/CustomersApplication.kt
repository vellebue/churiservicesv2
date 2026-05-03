package org.bastanchu.churiservicesv2.customers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.bastanchu.churiservicesv2"])
class CustomersApplication

fun main(args: Array<String>) {
    runApplication<CustomersApplication>(*args)
}
