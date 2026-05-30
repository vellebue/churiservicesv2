package org.bastanchu.churiservicesv2.common.infrastructure.logging

import org.slf4j.MDC
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class TraceparentPropagationInterceptor : ClientHttpRequestInterceptor {

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        val correlationId = MDC.get(CorrelationIdFilter.MDC_CORRELATION_ID)
        if (!correlationId.isNullOrBlank() && !request.headers.containsKey(CorrelationIdFilter.HEADER_TRACEPARENT)) {
            request.headers.set(CorrelationIdFilter.HEADER_TRACEPARENT, buildTraceparent(correlationId))
        }
        return execution.execute(request, body)
    }

    private fun buildTraceparent(traceId: String): String {
        val spanId = generateSpanId()
        return "00-$traceId-$spanId-01"
    }

    private fun generateSpanId(): String {
        val bytes = ByteArray(8)
        RANDOM.nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private val RANDOM = SecureRandom()
    }
}
