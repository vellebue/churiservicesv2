package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.PingUseCase
import org.bastanchu.churiservicesv2.customers.domain.DatabaseHealthChecker
import org.bastanchu.churiservicesv2.common.application.dto.PingStatusDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class PingService(
    private val databaseHealthChecker: DatabaseHealthChecker,
    private val buildProperties: BuildProperties,
    @Value("\${info.app.name:churiservices-customers}") private val applicationName: String
) : PingUseCase {

    override fun execute(fullMode: Boolean): PingStatusDto {
        val timestamp = currentTimestamp()
        val rootStatus = PingStatusDto(
            componentName = applicationName,
            componentType = PingStatusDto.ComponentType.SPRING_BOOT_APP,
            version = buildProperties.version,
            status = PingStatusDto.Status.RUNNING,
            timestamp = timestamp,
            dependencies = mutableListOf()
        )
        if (fullMode) {
            rootStatus.dependencies.add(buildDatabaseStatus(timestamp))
        }
        return rootStatus
    }

    private fun buildDatabaseStatus(timestamp: String): PingStatusDto {
        val info = databaseHealthChecker.check()
        return PingStatusDto(
            componentName = info.componentName,
            componentType = PingStatusDto.ComponentType.POSTGRESQL_DB,
            version = info.version,
            status = if (info.running) PingStatusDto.Status.RUNNING else PingStatusDto.Status.NOT_AVAILABLE,
            timestamp = timestamp,
            dependencies = mutableListOf()
        )
    }

    private fun currentTimestamp(): String =
        ZonedDateTime.now().format(TIMESTAMP_FORMATTER)

    companion object {
        private val TIMESTAMP_FORMATTER: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
    }
}
