package org.bastanchu.churiservicesv2.common.application.dto

import com.fasterxml.jackson.annotation.JsonValue
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "PingStatusDto",
    description = "Data Object to describe the running status for a component in our Docker/Kubernetes network.",
    example = """
            {
                "componentName": "churiservices-articles",
                "componentType": "Spring Boot Application",
                "version": "1.0-SNAPSHOT",
                "status": "RUNNING",
                "timestamp": "2023-12-07 18:21:07 +0100",
                "dependencies": [
                    {
                        "componentName": "churiservices-orders-db",
                        "componentType": "Postgresql Database",
                        "version": "15.4",
                        "status": "RUNNING",
                        "timestamp": "2023-12-07 18:21:07 +0100",
                        "dependencies": []
                    }
                ]
            }
        """)
data class PingStatusDto(
    @field:Schema(description = "The name of this component", example = "churiservices-orders" , required = true, defaultValue = "")
    var componentName : String = "",
    @field:Schema(description = "The type of this component", type = "PingStatus.ComponentType", example = "Spring Boot Application" , required = true, defaultValue = "Spring Boot Application")
    var componentType : ComponentType = ComponentType.SPRING_BOOT_APP,
    @field:Schema(description = "Running version for this component", example = "1.0-SNAPSHOT", required = true, defaultValue = "")
    var version : String = "",
    @field:Schema(description = "Running status for this component", type = "PingStatus.Status", example = "RUNNING", required = true)
    var status: Status = Status.SHUTDOWN,
    @field:Schema(description = "Timestamp for this HTTP response", example = "2023-12-07 18:05:09 +0100", required = true, defaultValue = "1970-01-01 00:00:00 +0000")
    var timestamp: String = "1970-01-01 00:00:00 +0000",
    @field:ArraySchema(schema = Schema(description = "Related subsystems that are required for this system", required = false))
    var dependencies : MutableList<PingStatusDto> = ArrayList<PingStatusDto>()
)
{
    enum class Status(val statusName : String) {
        RUNNING("RUNNING"),
        SHUTDOWN("SHUTDOWN"),
        NOT_AVAILABLE("NOT AVAILABLE");

        @JsonValue
        override fun toString() : String {
            return statusName
        }
    }

    enum class ComponentType(val type : String) {
        SPRING_BOOT_APP("Spring Boot Application"),
        POSTGRESQL_DB("Postgresql Database");

        @JsonValue
        override fun toString(): String {
            return type
        }
    }
}