package cz.smartbrains.qesu.module.common.service

import java.util.*

interface Messages {
    fun getMessage(code: String): String
    fun getMessage(code: String, vararg args: Any?): String
    fun formatCurrency(number: Int, currency: String): String

    companion object {
        @JvmField
        val SK_LOCALE: Locale = Locale.forLanguageTag("sk-SK")
        @JvmField
        val DEFAULT_LOCALE = SK_LOCALE
        @JvmField
        val CZ_LOCALE: Locale = Locale.forLanguageTag("cs-CZ")
    }
}