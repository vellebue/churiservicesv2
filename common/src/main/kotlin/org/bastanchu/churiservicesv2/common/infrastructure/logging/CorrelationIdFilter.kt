package org.bastanchu.churiservicesv2.common.infrastructure.logging

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.security.SecureRandom
import java.util.UUID

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorrelationIdFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val correlationId = extractOrGenerateCorrelationId(request)
        val transactionId = UUID.randomUUID().toString()
        try {
            MDC.put(MDC_CORRELATION_ID, correlationId)
            MDC.put(MDC_TRANSACTION_ID, transactionId)
            response.setHeader(HEADER_TRACEPARENT, buildTraceparent(correlationId))
            filterChain.doFilter(request, response)
        } finally {
            MDC.remove(MDC_CORRELATION_ID)
            MDC.remove(MDC_TRANSACTION_ID)
        }
    }

    private fun extractOrGenerateCorrelationId(request: HttpServletRequest): String {
        val incoming = request.getHeader(HEADER_TRACEPARENT)
        val parsedTraceId = parseTraceId(incoming)
        if (parsedTraceId != null) return parsedTraceId
        val legacy = request.getHeader(HEADER_X_CORRELATION_ID)
        if (!legacy.isNullOrBlank()) return legacy
        return generateTraceId()
    }

    private fun parseTraceId(traceparent: String?): String? {
        if (traceparent.isNullOrBlank()) return null
        val parts = traceparent.split("-")
        if (parts.size < 4) return null
        val traceId = parts[1]
        return if (traceId.length == 32 && traceId.all { it in '0'..'9' || it in 'a'..'f' }) traceId else null
    }

    private fun buildTraceparent(traceId: String): String {
        val spanId = generateSpanId()
        return "00-$traceId-$spanId-01"
    }

    private fun generateTraceId(): String {
        val bytes = ByteArray(16)
        RANDOM.nextBytes(bytes)
        return bytes.toHex()
    }

    private fun generateSpanId(): String {
        val bytes = ByteArray(8)
        RANDOM.nextBytes(bytes)
        return bytes.toHex()
    }

    private fun ByteArray.toHex(): String =
        joinToString("") { "%02x".format(it) }

    companion object {
        const val MDC_CORRELATION_ID = "correlationId"
        const val MDC_TRANSACTION_ID = "transactionId"
        const val HEADER_TRACEPARENT = "traceparent"
        const val HEADER_X_CORRELATION_ID = "X-Correlation-ID"
        private val RANDOM = SecureRandom()
    }
}
