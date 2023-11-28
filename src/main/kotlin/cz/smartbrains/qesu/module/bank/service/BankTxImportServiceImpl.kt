package cz.smartbrains.qesu.module.bank.service

import cz.smartbrains.qesu.module.bank.entity.BankTxImport_
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.bank.dto.BankTxImportDto
import cz.smartbrains.qesu.module.bank.entity.BankTxImport
import cz.smartbrains.qesu.module.bank.mapper.BankTxImportMapper
import cz.smartbrains.qesu.module.bank.repository.BankTxImportRepository
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class BankTxImportServiceImpl(private val txImportRepository: BankTxImportRepository,
                              private val txImportMapper: BankTxImportMapper,
                              private val camtImportService: CamtImportService) : BankTxImportService {

    override fun findAll(): List<BankTxImportDto> {
        return txImportRepository
                .findAll(Sort.by(BankTxImport_.CREATED).descending())
                .stream()
                .map { txImport: BankTxImport -> txImportMapper.doToDto(txImport)!! }
                .collect(Collectors.toList())
    }

    override fun create(bankTxImport: BankTxImportDto, user: AlfaUserDetails): BankTxImportDto {
        val bankTxImports: List<BankTxImportDto> = camtImportService.importTransactions(bankTxImport.document!!.id!!, user)
        return bankTxImports
                .stream()
                .findFirst()
                .orElseThrow { ServiceRuntimeException("bank.transaction.import.invalid.input.file") }
    }

}