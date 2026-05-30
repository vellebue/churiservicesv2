package org.bastanchu.churiservicesv2.common.infrastructure.logging

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class LogEntry(
    val value: String = ""
)
