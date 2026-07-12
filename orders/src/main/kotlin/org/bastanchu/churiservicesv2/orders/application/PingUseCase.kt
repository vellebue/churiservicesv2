package org.bastanchu.churiservicesv2.orders.application

import org.bastanchu.churiservicesv2.common.application.dto.PingStatusDto

interface PingUseCase {
    fun execute(fullMode: Boolean): PingStatusDto
}
