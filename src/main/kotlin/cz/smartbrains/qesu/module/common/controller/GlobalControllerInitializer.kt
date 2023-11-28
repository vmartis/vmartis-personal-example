package cz.smartbrains.qesu.module.common.controller

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Lists
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.user.validation.ValidationException
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.InitBinder
import java.beans.PropertyEditorSupport
import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@ControllerAdvice
class GlobalControllerInitializer {
    @ExceptionHandler(ValidationException::class)
    fun validation(e: ValidationException): ResponseEntity<Map<String, Any?>> {
        log.warn("Validation error.", e)
        val body: MutableMap<String, Any?> = HashMap()
        body["message"] = e.message
        if (e.errors != null) {
            body["errors"] = e.errors.allErrors
        }
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun dataIntegrity(e: DuplicateKeyException): ResponseEntity<Map<String, Any?>> {
        log.warn("Validation error.", e)
        val body: MutableMap<String, Any?> = HashMap()
        body["message"] = e.message
        val errors: MutableList<Map<String, String>> = Lists.newArrayList()
        errors.add(ImmutableMap.of("code", DATA_INTEGRITY_VIOLATION_DUPLICATE))
        body["errors"] = errors
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ServiceRuntimeException::class)
    fun serviceRuntime(e: ServiceRuntimeException): ResponseEntity<Map<String, Any?>> {
        log.error("Service error.", e)
        val body: MutableMap<String, Any?> = HashMap()
        body["message"] = e.message
        body["args"] = e.args
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataAccessException::class)
    fun dataIntegrity(e: DataAccessException): ResponseEntity<Map<String, Any?>> {
        log.error("Data integrity error.", e)
        val body: MutableMap<String, Any?> = HashMap()
        body["message"] = e.message
        val errors: MutableList<Map<String, String>> = Lists.newArrayList()
        ExceptionUtils.getThrowableList(e)
                .stream()
                .filter { throwable: Throwable? -> throwable is SQLException }
                .findFirst()
                .ifPresent { throwable: Throwable ->
                    when ((throwable as SQLException).sqlState) {
                        "23505" -> errors.add(ImmutableMap.of("code", DATA_INTEGRITY_VIOLATION_DUPLICATE))
                        "23503" -> errors.add(ImmutableMap.of("code", DATA_INTEGRITY_VIOLATION_REFERENCED))
                    }
                }
        body["errors"] = errors
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDenied(e: AccessDeniedException?): ResponseEntity<Map<String, Any>> {
        log.error("Access denied error.", e)
        val body: MutableMap<String, Any> = HashMap()
        body["message"] = NOT_ALLOWED
        return ResponseEntity(body, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler
    fun exception(e: Exception): ResponseEntity<Map<String, Any?>> {
        log.error("Not categorized error.", e)
        val body: MutableMap<String, Any?> = HashMap()
        body["message"] = e.message
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(LocalDate::class.java, object : PropertyEditorSupport() {
            @Throws(IllegalArgumentException::class)
            override fun setAsText(text: String) {
                value = LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
            }
        })
    }

    companion object {
        private val log = LoggerFactory.getLogger(GlobalControllerInitializer::class.java)
        const val DATA_INTEGRITY_VIOLATION = "data.integrity.violation"
        const val DATA_INTEGRITY_VIOLATION_DUPLICATE = "$DATA_INTEGRITY_VIOLATION.duplicate"
        const val DATA_INTEGRITY_VIOLATION_REFERENCED = "$DATA_INTEGRITY_VIOLATION.referenced"
        const val NOT_ALLOWED = "not.allowed"
    }
}