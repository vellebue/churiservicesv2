package org.bastanchu.churiservicesv2.articles.infrastructure.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.Locale

@Configuration
class MessageConfig {

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }

    @Bean
    fun localeResolver(): LocaleResolver {
        val resolver = AcceptHeaderLocaleResolver()
        resolver.setDefaultLocale(Locale.ENGLISH)
        resolver.supportedLocales = listOf(
            Locale.ENGLISH,
            Locale("es"),
            Locale.FRENCH,
            Locale.GERMAN
        )
        return resolver
    }
}
