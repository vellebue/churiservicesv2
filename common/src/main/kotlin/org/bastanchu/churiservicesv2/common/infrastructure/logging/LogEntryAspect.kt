package org.bastanchu.churiservicesv2.common.infrastructure.logging

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Aspect
@Component
class LogEntryAspect(
    private val logRedactor: LogRedactor
) {

    @Around(
        "within(@org.bastanchu.churiservicesv2.common.infrastructure.logging.LogEntry *) " +
        "|| @annotation(org.bastanchu.churiservicesv2.common.infrastructure.logging.LogEntry)"
    )
    fun logEntry(joinPoint: ProceedingJoinPoint): Any? {
        val method = (joinPoint.signature as MethodSignature).method
        val targetClass = joinPoint.target?.javaClass ?: method.declaringClass
        val annotation = resolveAnnotation(method, targetClass)
        val entryPointName = if (annotation?.value?.isNotBlank() == true) {
            annotation.value
        } else {
            "${targetClass.simpleName}#${method.name}"
        }
        val logger = LoggerFactory.getLogger(targetClass)

        logger.info("Entry-point invoked: {}", entryPointName)
        if (logger.isDebugEnabled) {
            val payload = describeArguments(method, joinPoint.args)
            logger.debug("Entry-point {} payload: {}", entryPointName, payload)
        }

        val start = System.nanoTime()
        return try {
            val result = joinPoint.proceed()
            val elapsedMs = (System.nanoTime() - start) / 1_000_000
            logger.info("Entry-point {} completed in {} ms", entryPointName, elapsedMs)
            result
        } catch (ex: Throwable) {
            val elapsedMs = (System.nanoTime() - start) / 1_000_000
            logger.warn("Entry-point {} failed after {} ms: {}", entryPointName, elapsedMs, ex.toString())
            throw ex
        }
    }

    private fun resolveAnnotation(method: Method, targetClass: Class<*>): LogEntry? {
        method.getAnnotation(LogEntry::class.java)?.let { return it }
        return targetClass.getAnnotation(LogEntry::class.java)
    }

    private fun describeArguments(method: Method, args: Array<Any?>): String {
        if (args.isEmpty()) return "[]"
        val params = method.parameters
        val rendered = args.mapIndexed { index, arg ->
            val name = params.getOrNull(index)?.name ?: "arg$index"
            "\"$name\":${logRedactor.redact(arg)}"
        }
        return rendered.joinToString(prefix = "{", postfix = "}", separator = ",")
    }
}
