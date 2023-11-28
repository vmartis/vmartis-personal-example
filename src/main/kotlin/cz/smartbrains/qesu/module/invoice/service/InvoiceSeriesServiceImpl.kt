package cz.smartbrains.qesu.module.invoice.service

import com.google.common.base.CharMatcher
import com.google.common.base.Preconditions
import cz.smartbrains.qesu.module.invoice.entity.InvoiceSeries_
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.invoice.dto.InvoiceSeriesDto
import cz.smartbrains.qesu.module.invoice.entity.InvoiceSeries
import cz.smartbrains.qesu.module.invoice.entity.InvoiceSeriesNumber
import cz.smartbrains.qesu.module.invoice.mapper.InvoiceSeriesMapper
import cz.smartbrains.qesu.module.invoice.repository.InvoiceSeriesRepository
import cz.smartbrains.qesu.module.invoice.type.InvoiceSeriesFormat
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
internal class InvoiceSeriesServiceImpl(private val invoiceSeriesRepository: InvoiceSeriesRepository,
                                        private val invoiceSeriesMapper: InvoiceSeriesMapper,
                                        private val orderableEntityService: OrderableEntityService) : InvoiceSeriesService {
    @Transactional(readOnly = true)
    override fun findAll(): List<InvoiceSeriesDto> {
        return invoiceSeriesRepository
                .findAll(Sort.by(InvoiceSeries_.ORDER).ascending())
                .stream()
                .map { location: InvoiceSeries? -> invoiceSeriesMapper.doToDto(location)!! }
                .collect(Collectors.toList())
    }

    @Transactional
    override fun create(invoiceSeries: InvoiceSeriesDto): InvoiceSeriesDto {
        val entity = invoiceSeriesMapper.dtoToDo(invoiceSeries)
        entity!!.active = true
        entity.defaultFlag = invoiceSeries.defaultFlag
        entity.order = orderableEntityService.newItemOrder(invoiceSeriesRepository)
        val savedEntity = invoiceSeriesRepository.save(entity)
        return invoiceSeriesMapper.doToDto(savedEntity)!!
    }

    @Transactional
    override fun update(invoiceSeries: InvoiceSeriesDto): InvoiceSeriesDto {
        val invoiceSeriesEntity = invoiceSeriesRepository.findById(invoiceSeries.id!!).orElseThrow { RecordNotFoundException() }
        invoiceSeriesEntity.name = invoiceSeries.name
        invoiceSeriesEntity.index = invoiceSeries.index
        invoiceSeriesEntity.prefix = invoiceSeries.prefix
        invoiceSeriesEntity.format = invoiceSeries.format
        invoiceSeriesEntity.year = invoiceSeries.year
        invoiceSeriesEntity.active = invoiceSeries.active
        invoiceSeriesEntity.defaultFlag = invoiceSeries.defaultFlag
        return invoiceSeriesMapper.doToDto(invoiceSeriesEntity)!!
    }

    @Transactional
    override fun updatePosition(invoiceSeriesId: Long, position: Int): InvoiceSeriesDto {
        val invoiceSeries = orderableEntityService.updatePosition(invoiceSeriesRepository, invoiceSeriesId!!, position)
        return invoiceSeriesMapper.doToDto(invoiceSeries)!!
    }

    @Transactional
    override fun nextNumber(id: Long): InvoiceSeriesNumber {
        Preconditions.checkNotNull(id)
        val invoiceSeries = invoiceSeriesRepository.findById(id).orElseThrow { RecordNotFoundException() }

        //extract only numbers from prefix
        var prefixNumbers = CharMatcher.inRange('0', '9').retainFrom(invoiceSeries.prefix!!)
        //take max last 2 digits
        prefixNumbers = prefixNumbers.substring(Math.max(prefixNumbers.length - 2, 0))
        //take max last 4 digits
        var indexNumbers = String.format("%04d", invoiceSeries.index)
        indexNumbers = indexNumbers.substring(Math.max(indexNumbers.length - 4, 0))
        //take max last 4 digits
        var yearNumbers = String.format("%04d", invoiceSeries.year)
        yearNumbers = yearNumbers.substring(Math.max(yearNumbers.length - 4, 0))
        val number: String
        val variableSymbol: String
        when (invoiceSeries.format) {
            InvoiceSeriesFormat.PREFIX_NNNN_YYYY -> {
                number = String.format("%s%s%s", invoiceSeries.prefix, indexNumbers, yearNumbers)
                variableSymbol = String.format("%s%s%s", prefixNumbers, indexNumbers, yearNumbers)
            }
            InvoiceSeriesFormat.PREFIX_YYYY_NNNN -> {
                number = String.format("%s%s%s", invoiceSeries.prefix, yearNumbers, indexNumbers)
                variableSymbol = String.format("%s%s%s", prefixNumbers, yearNumbers, indexNumbers)
            }
            else -> throw IllegalStateException("Invalid invoice format")
        }
        invoiceSeries.index = invoiceSeries.index + 1
        return InvoiceSeriesNumber(number, variableSymbol)
    }
}