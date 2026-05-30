package org.bastanchu.churiservicesv2.common.infrastructure.logging

import org.springframework.boot.web.client.RestClientCustomizer
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientLoggingConfig(
    private val traceparentPropagationInterceptor: TraceparentPropagationInterceptor
) {

    @Bean
    fun traceparentRestClientCustomizer(): RestClientCustomizer =
        RestClientCustomizer { builder: RestClient.Builder ->
            builder.requestInterceptor(traceparentPropagationInterceptor)
        }

    @Bean
    fun traceparentRestTemplateCustomizer(): RestTemplateCustomizer =
        RestTemplateCustomizer { template ->
            template.interceptors.add(traceparentPropagationInterceptor)
        }
}
