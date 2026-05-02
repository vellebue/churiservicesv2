package org.bastanchu.churiservicesv2.articles.infrastructure.health

import org.bastanchu.churiservicesv2.articles.domain.DatabaseHealthChecker
import org.bastanchu.churiservicesv2.articles.domain.DatabaseHealthInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class PostgresqlHealthChecker(
    private val dataSource: DataSource,
    @Value("\${info.app.dependencies.database.name:churiservices-articles-db}")
    private val componentName: String
) : DatabaseHealthChecker {

    override fun check(): DatabaseHealthInfo {
        return try {
            dataSource.connection.use { connection ->
                val version = connection.metaData.databaseProductVersion
                DatabaseHealthInfo(
                    running = true,
                    componentName = componentName,
                    version = version
                )
            }
        } catch (ex: Exception) {
            DatabaseHealthInfo(
                running = false,
                componentName = componentName,
                version = "unknown"
            )
        }
    }
}
