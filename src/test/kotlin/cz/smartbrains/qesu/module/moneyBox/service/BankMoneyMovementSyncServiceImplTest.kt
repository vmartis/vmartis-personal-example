package cz.smartbrains.qesu.module.moneyBox.service

import com.google.common.collect.Lists
import org.mockito.kotlin.any
import cz.smartbrains.qesu.module.bank.dto.BankAccountDto
import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import cz.smartbrains.qesu.module.bank.mapper.BankTransactionMapper
import cz.smartbrains.qesu.module.bank.repository.BankAccountRepository
import cz.smartbrains.qesu.module.bank.repository.BankTransactionRepository
import cz.smartbrains.qesu.module.bank.service.BankAccountService
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyBox
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyMovementRepository
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class BankMoneyMovementSyncServiceImplTest {
    @Mock
    private val bankTransactionRepository: BankTransactionRepository? = null

    @Mock
    private val bankTransactionMapper: BankTransactionMapper? = null

    @Mock
    private val bankMoneyMovementRepository: BankMoneyMovementRepository? = null

    @Mock
    private val bankAccountService: BankAccountService? = null

    @Mock
    private val bankAccountRepository: BankAccountRepository? = null

    @Mock
    private val bankMoneyBoxRepository: BankMoneyBoxRepository? = null

    @Mock
    private val bankMoneyBoxService: BankMoneyBoxService? = null

    @Mock
    private val user: AlfaUserDetails? = null
    private var service: BankMoneyMovementSyncService? = null
    @BeforeEach
    fun setUp() {
        service = BankMoneyMovementSyncServiceImpl(bankTransactionRepository!!,
                bankTransactionMapper!!,
                bankMoneyMovementRepository!!,
                bankMoneyBoxService!!,
                bankMoneyBoxRepository!!,
                bankAccountService!!,
                bankAccountRepository!!)
        val bankMoneyBox = BankMoneyBox()
        bankMoneyBox.initialDate = LocalDate.now()
        Mockito.`when`(bankMoneyBoxRepository.findByBankAccountId(BANK_ACCOUNT_ID)).thenReturn(Optional.of(bankMoneyBox))
    }

    @Test
    fun synchronize_noUnsynchronizedTx_noMovementCreated() {
        Mockito.`when`(bankTransactionRepository!!.findAllUnmapped(any(), Mockito.anyInt())).thenReturn(emptyList())
        service!!.synchronize(BANK_ACCOUNT_ID, user)
        Mockito.verify(bankMoneyMovementRepository, Mockito.never())!!.saveAll(ArgumentMatchers.anyCollection())
        Mockito.verify(bankMoneyMovementRepository, Mockito.never())!!.flush()
    }

    @Test
    fun synchronize_oneUnsynchronisedTx_newMovementCreated() {
        val bankAccountId = 1L
        val transactionId = 2L
        val moneyBoxId = 3L
        val correspondingAccountId = "123456789"
        val correspondingBankId = "0900"
        val correspondingIban = "SK0987654321"
        val bankTransaction = BankTransaction()
        bankTransaction.id = transactionId
        val transformedMovement = BankMoneyMovement()
        val parsedCorrespondingAccount = BankAccountDto()
        val correspondingBankAccount = BankAccount()
        parsedCorrespondingAccount.accountId = correspondingAccountId
        parsedCorrespondingAccount.bankId = correspondingBankId
        parsedCorrespondingAccount.iban = correspondingIban
        val moneyBox = BankMoneyBox()
        moneyBox.id = moneyBoxId
        transformedMovement.moneyBox = moneyBox
        Mockito.`when`(bankTransactionRepository!!.findAllUnmapped(any(), Mockito.anyInt())).thenReturn(Lists.newArrayList(bankTransaction)).thenReturn(emptyList())
        Mockito.`when`(bankTransactionMapper!!.doToMovement(bankTransaction, user!!)).thenReturn(transformedMovement)
        Mockito.`when`(bankAccountService!!.parseCorrespondingAccount(transactionId)).thenReturn(parsedCorrespondingAccount)
        Mockito.`when`(bankAccountRepository!!.findByAccountIdAndBankId(correspondingAccountId, correspondingBankId)).thenReturn(Optional.of(correspondingBankAccount))
        service!!.synchronize(bankAccountId, user)
        Mockito.verify(bankMoneyMovementRepository)!!.saveAll(Lists.newArrayList(transformedMovement))
        Assertions.assertThat(transformedMovement.correspondingBankAccount).isSameAs(correspondingBankAccount)
        Mockito.verify(bankMoneyMovementRepository)!!.flush()
        Mockito.verify(bankMoneyBoxService)!!.updateAccount(moneyBoxId)
    }

    companion object {
        private const val BANK_ACCOUNT_ID = 1L
    }
}