package org.bastanchu.churiservicesv2.common.infrastructure.logging

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class LogRedactor(
    private val objectMapper: ObjectMapper,
    @Value("\${logging.redaction.denylist:authorization,password,token,secret,apiKey,creditCard,ssn}")
    denylistCsv: String
) {

    private val denylist: Set<String> =
        denylistCsv.split(",").map { it.trim().lowercase() }.filter { it.isNotEmpty() }.toSet()

    fun redact(value: Any?): String {
        if (value == null) return "null"
        return try {
            val tree = objectMapper.valueToTree<JsonNode>(value)
            redactNode(tree)
            objectMapper.writeValueAsString(tree)
        } catch (ex: Exception) {
            "<unserializable: ${value::class.simpleName}>"
        }
    }

    private fun redactNode(node: JsonNode) {
        when (node) {
            is ObjectNode -> {
                val fieldNames = node.fieldNames().asSequence().toList()
                for (name in fieldNames) {
                    if (denylist.contains(name.lowercase())) {
                        node.put(name, MASK)
                    } else {
                        redactNode(node.get(name))
                    }
                }
            }
            is ArrayNode -> node.forEach { redactNode(it) }
            else -> { /* leaf node, nothing to redact */ }
        }
    }

    companion object {
        private const val MASK = "***"
    }
}
