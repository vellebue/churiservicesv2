package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.service.PingService
import org.bastanchu.churiservicesv2.articles.domain.DatabaseHealthChecker
import org.bastanchu.churiservicesv2.articles.domain.DatabaseHealthInfo
import org.bastanchu.churiservicesv2.common.application.dto.PingStatusDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.info.BuildProperties
import java.util.Properties

@ExtendWith(MockitoExtension::class)
class PingUseCaseTest {

    @Mock
    private lateinit var databaseHealthChecker: DatabaseHealthChecker

    private lateinit var useCase: PingService

    @BeforeEach
    fun setup() {
        val properties = Properties().apply {
            setProperty("version", "0.0.1-SNAPSHOT")
            setProperty("name", "articles")
        }
        useCase = PingService(
            databaseHealthChecker = databaseHealthChecker,
            buildProperties = BuildProperties(properties),
            applicationName = "churiservices-articles"
        )
    }

    @Test
    fun `should return root status only when fullMode is false`() {
        val result = useCase.execute(fullMode = false)

        assertEquals("churiservices-articles", result.componentName)
        assertEquals(PingStatusDto.ComponentType.SPRING_BOOT_APP, result.componentType)
        assertEquals("0.0.1-SNAPSHOT", result.version)
        assertEquals(PingStatusDto.Status.RUNNING, result.status)
        assertNotNull(result.timestamp)
        assertTrue(result.dependencies.isEmpty())
        verify(databaseHealthChecker, never()).check()
    }

    @Test
    fun `should include running database dependency when fullMode is true`() {
        `when`(databaseHealthChecker.check()).thenReturn(
            DatabaseHealthInfo(
                running = true,
                componentName = "churiservices-articles-db",
                version = "16.2"
            )
        )

        val result = useCase.execute(fullMode = true)

        assertEquals(1, result.dependencies.size)
        val dbStatus = result.dependencies[0]
        assertEquals("churiservices-articles-db", dbStatus.componentName)
        assertEquals(PingStatusDto.ComponentType.POSTGRESQL_DB, dbStatus.componentType)
        assertEquals("16.2", dbStatus.version)
        assertEquals(PingStatusDto.Status.RUNNING, dbStatus.status)
        assertEquals(result.timestamp, dbStatus.timestamp)
        assertTrue(dbStatus.dependencies.isEmpty())
    }

    @Test
    fun `should report database NOT_AVAILABLE when health check reports it down`() {
        `when`(databaseHealthChecker.check()).thenReturn(
            DatabaseHealthInfo(
                running = false,
                componentName = "churiservices-articles-db",
                version = "unknown"
            )
        )

        val result = useCase.execute(fullMode = true)

        assertEquals(PingStatusDto.Status.RUNNING, result.status)
        assertEquals(1, result.dependencies.size)
        val dbStatus = result.dependencies[0]
        assertEquals(PingStatusDto.Status.NOT_AVAILABLE, dbStatus.status)
        assertEquals("unknown", dbStatus.version)
        assertFalse(result.dependencies.isEmpty())
    }
}
