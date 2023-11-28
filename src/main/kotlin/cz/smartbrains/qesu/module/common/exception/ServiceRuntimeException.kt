package cz.smartbrains.qesu.module.common.exception

open class ServiceRuntimeException(message: String?, val args: List<Any?>) : RuntimeException(message) {

    constructor(message: String?, vararg args: Any?) : this(message, listOf<Any?>(*args))

    companion object {
        private const val serialVersionUID = -6950581036178135603L
    }
}