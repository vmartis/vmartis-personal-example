package cz.smartbrains.qesu.logging

import com.google.common.base.Joiner
import com.google.common.collect.Iterables
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.stereotype.Component

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
class AspectLogger {
    @Around("within(cz.smartbrains.qesu..*) && bean(*Service*)")
    @Throws(Throwable::class)
    fun logFunctions(joinPoint: ProceedingJoinPoint): Any? {
        val sourceClass: Class<*> = try {
            joinPoint.target.javaClass
        } catch (ex: Exception) {
            javaClass
        }
        val logger = LoggerFactory.getLogger(sourceClass)
        val signature = joinPoint.signature as MethodSignature
        val methodArgs = joinPoint.args
        val methodName = signature.method.name
        val parameters = if (methodArgs == null || methodArgs.isEmpty()) NONE else Joiner.on(", ").skipNulls().join(methodArgs)

        // generate input log
        if (logger.isInfoEnabled) {
            val inputLog = String.format("called %s with parameters (%s).", methodName, parameters)
            logger.info(inputLog)
        }

        // method is executed
        val proceed = joinPoint.proceed()

        // generate output log
        if (logger.isDebugEnabled) {
            val outputLog: String = if (proceed is Iterable<*>) {
                String.format("%s returns %s results.", methodName, Iterables.size(proceed))
            } else if (proceed != null) {
                String.format("%s returns %s.", methodName, proceed)
            } else {
                String.format("%s finished.", methodName)
            }
            logger.debug(outputLog)
        }
        return proceed
    }

    companion object {
        private const val NONE = " [none] "
    }
}