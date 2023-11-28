package cz.smartbrains.qesu.module.common.service

import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableMap
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.configuration.repository.ConfigurationRepository
import cz.smartbrains.qesu.module.configuration.type.LocalizationType
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.text.NumberFormat
import java.util.*
import javax.annotation.PostConstruct

@Service
class MessagesImpl(private val messageSource: MessageSource, private val configurationRepository: ConfigurationRepository) : Messages {

    private var currencyLocaleMap: Map<Currency, Locale>? = null
    private var localeMap: Map<LocalizationType, Locale>? = null

    @PostConstruct
    fun init() {
        val currencyLocaleMapBuilder = ImmutableMap.builder<Currency, Locale>()
        val localeMapBuilder = ImmutableMap.builder<LocalizationType, Locale>()
        currencyLocaleMapBuilder.put(Currency.getInstance("EUR"), Messages.SK_LOCALE)
        currencyLocaleMapBuilder.put(Currency.getInstance("CZK"), Messages.CZ_LOCALE)
        localeMapBuilder.put(LocalizationType.SK, Messages.SK_LOCALE)
        localeMapBuilder.put(LocalizationType.CS, Messages.CZ_LOCALE)
        currencyLocaleMap = currencyLocaleMapBuilder.build()
        localeMap = localeMapBuilder.build()
    }

    override fun getMessage(code: String): String {
        return this.getMessage(code, *(null as Array<Any?>?)!!)
    }

    override fun getMessage(code: String, vararg args: Any?): String {
        Preconditions.checkNotNull(code)
        val localization = configurationRepository.findAll().stream().findFirst().orElseThrow { RecordNotFoundException() }.application!!.localization
        return messageSource.getMessage(code.toLowerCase(), args, localeMap!!.getOrDefault(localization, Messages.DEFAULT_LOCALE))
    }

    override fun formatCurrency(number: Int, currency: String): String {
        val currencyInst = Currency.getInstance(currency)
        val format = NumberFormat.getCurrencyInstance(currencyLocaleMap!![currencyInst])
        format.currency = currencyInst
        return format.format(number.toLong())
    }
}