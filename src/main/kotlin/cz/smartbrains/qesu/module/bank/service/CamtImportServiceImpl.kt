package cz.smartbrains.qesu.module.bank.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.document.repository.DocumentRepository
import cz.smartbrains.qesu.module.moneyBox.service.BankMoneyMovementSyncService
import cz.smartbrains.qesu.module.bank.dto.BankTxImportDto
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import cz.smartbrains.qesu.module.bank.entity.BankTxImport
import cz.smartbrains.qesu.module.bank.mapper.BankTxImportMapper
import cz.smartbrains.qesu.module.bank.mapper.camt.BankTxImportCamtMapper
import cz.smartbrains.qesu.module.bank.repository.BankTransactionRepository
import cz.smartbrains.qesu.module.bank.repository.BankTxImportRepository
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.apache.commons.lang3.StringUtils
import org.iso.camt053.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream
import java.util.function.Function
import java.util.stream.Collectors
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.transform.stream.StreamSource

@Service
class CamtImportServiceImpl(private val bankTxImportRepository: BankTxImportRepository,
                            private val camtImportMapper: BankTxImportCamtMapper,
                            private val documentRepository: DocumentRepository,
                            private val bankTransactionRepository: BankTransactionRepository,
                            private val txImportMapper: BankTxImportMapper,
                            private val syncService: BankMoneyMovementSyncService) : CamtImportService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun importTransactions(documentId: Long, user: AlfaUserDetails): List<BankTxImportDto> {
        log.info("Import transactions from CAMT file.")
        val documentDb = documentRepository.findById(documentId).orElseThrow { RecordNotFoundException() }
        val jaxbContext: JAXBContext
        val bankTxImports: List<BankTxImport>
        try {
            jaxbContext = JAXBContext.newInstance(Document::class.java)
            val unmarshaller = jaxbContext.createUnmarshaller()
            val document = unmarshaller.unmarshal(
                    StreamSource(
                            ByteArrayInputStream(documentDb.content)), Document::class.java)
            bankTxImports = camtImportMapper.fromCamt(document.value, documentDb)
            for (bankTxImport in bankTxImports) {
                var successCount = 0
                var failedCount = 0
                bankTxImportRepository.saveAndFlush(bankTxImport)
                for (transaction in bankTxImport.transactions) {
                    if (bankTransactionRepository.countBy(bankTxImport.account!!.id!!, transaction.transactionId) > 0) {
                        failedCount++
                    } else if (StringUtils.isBlank(transaction.transactionId)) {
                        throw ServiceRuntimeException("bank-tx-import.catm.transactionId.unknown")
                    } else {
                        bankTransactionRepository.saveAndFlush<BankTransaction>(transaction)
                        successCount++
                    }
                }
                bankTxImport.successCount = successCount
                bankTxImport.failedCount = failedCount
                //synchronize with money boxes
                syncService.synchronize(bankTxImport.account!!.id!!, user)
            }
        } catch (e: JAXBException) {
            log.error("Document unmarshalling failed.", e)
            throw ServiceRuntimeException("bank.transaction.camt.import.deserialize.failed")
        }
        return bankTxImports
                .stream()
                .map(Function { txImport: BankTxImport -> txImportMapper.doToDto(txImport)!! })
                .collect(Collectors.toList())!!
    }
}