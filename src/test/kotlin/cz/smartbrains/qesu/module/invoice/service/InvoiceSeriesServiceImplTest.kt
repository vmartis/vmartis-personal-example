package cz.smartbrains.qesu.module.invoice.service

import org.mockito.kotlin.any
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.invoice.entity.InvoiceSeries
import cz.smartbrains.qesu.module.invoice.mapper.InvoiceSeriesMapper
import cz.smartbrains.qesu.module.invoice.repository.InvoiceSeriesRepository
import cz.smartbrains.qesu.module.invoice.type.InvoiceSeriesFormat
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class InvoiceSeriesServiceImplTest {
    @Mock
    private val invoiceSeriesRepository: InvoiceSeriesRepository? = null

    @Mock
    private val invoiceSeriesMapper: InvoiceSeriesMapper? = null

    @Mock
    private val invoiceSeries: InvoiceSeries? = null

    @Mock
    private val orderableEntityService: OrderableEntityService? = null
    private var service: InvoiceSeriesService? = null

    @BeforeEach
    fun setUp() {
        service = InvoiceSeriesServiceImpl(invoiceSeriesRepository!!, invoiceSeriesMapper!!, orderableEntityService!!)
        Mockito.`when`(invoiceSeriesRepository.findById(any())).thenReturn(Optional.of(invoiceSeries!!))
    }

    @Test
    fun nextNumber_emptyPrefix_isAccepted() {
        Mockito.`when`(invoiceSeries!!.prefix).thenReturn("")
        Mockito.`when`(invoiceSeries.index).thenReturn(1)
        Mockito.`when`(invoiceSeries.year).thenReturn(2)
        Mockito.`when`(invoiceSeries.format).thenReturn(InvoiceSeriesFormat.PREFIX_NNNN_YYYY)
        val generatedNumber = service!!.nextNumber(1L)
        Assertions.assertThat(generatedNumber.invoiceNumber).isEqualTo("00010002")
        Assertions.assertThat(generatedNumber.variableSymbol).isEqualTo("00010002")
    }

    @Test
    fun nextNumber_manyDigitsInPrefix_lastTwoAreUsed() {
        Mockito.`when`(invoiceSeries!!.prefix).thenReturn("ABC112233-")
        Mockito.`when`(invoiceSeries.index).thenReturn(1000)
        Mockito.`when`(invoiceSeries.year).thenReturn(2222)
        Mockito.`when`(invoiceSeries.format).thenReturn(InvoiceSeriesFormat.PREFIX_NNNN_YYYY)
        val generatedNumber = service!!.nextNumber(1L)
        Assertions.assertThat(generatedNumber.invoiceNumber).isEqualTo("ABC112233-10002222")
        Assertions.assertThat(generatedNumber.variableSymbol).isEqualTo("3310002222")
    }

    @Test
    fun nextNumber_indexAndYearHave5digits_last4AreUsed() {
        Mockito.`when`(invoiceSeries!!.prefix).thenReturn("VF19-")
        Mockito.`when`(invoiceSeries.index).thenReturn(111115)
        Mockito.`when`(invoiceSeries.year).thenReturn(222225)
        Mockito.`when`(invoiceSeries.format).thenReturn(InvoiceSeriesFormat.PREFIX_NNNN_YYYY)
        val generatedNumber = service!!.nextNumber(1L)
        Assertions.assertThat(generatedNumber.invoiceNumber).isEqualTo("VF19-11152225")
        Assertions.assertThat(generatedNumber.variableSymbol).isEqualTo("1911152225")
    }

    @Test
    fun nextNumber_differentFormatUsed_correctOutputReturned() {
        Mockito.`when`(invoiceSeries!!.prefix).thenReturn("VF-")
        Mockito.`when`(invoiceSeries.index).thenReturn(1)
        Mockito.`when`(invoiceSeries.year).thenReturn(2020)
        Mockito.`when`(invoiceSeries.format).thenReturn(InvoiceSeriesFormat.PREFIX_NNNN_YYYY)
        var generatedNumber = service!!.nextNumber(1L)
        Assertions.assertThat(generatedNumber.invoiceNumber).isEqualTo("VF-00012020")
        Assertions.assertThat(generatedNumber.variableSymbol).isEqualTo("00012020")
        Mockito.`when`(invoiceSeries.format).thenReturn(InvoiceSeriesFormat.PREFIX_YYYY_NNNN)
        generatedNumber = service!!.nextNumber(1L)
        Assertions.assertThat(generatedNumber.invoiceNumber).isEqualTo("VF-20200001")
        Assertions.assertThat(generatedNumber.variableSymbol).isEqualTo("20200001")
    }
}