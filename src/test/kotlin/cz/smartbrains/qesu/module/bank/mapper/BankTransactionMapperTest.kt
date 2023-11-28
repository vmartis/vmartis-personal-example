package cz.smartbrains.qesu.module.bank.mapper

import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyBox
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyBoxRepository
import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class BankTransactionMapperTest {
    @Mock
    private val moneyBoxRepository: BankMoneyBoxRepository? = null

    @Mock
    private val moneyBox: BankMoneyBox? = null

    @Mock
    private val user: AlfaUserDetails? = null

    @Mock
    private val userRepository: UserRepository? = null

    private var mapper: BankTransactionMapper? = null
    private val bankTransaction = BankTransaction()

    @BeforeEach
    fun setUp() {
        mapper = BankTransactionMapperImpl()
        mapper!!.moneyBoxRepository = moneyBoxRepository
        mapper!!.userRepository = userRepository

        val bankAccountId = 1L
        val account = BankAccount()
        account.id = bankAccountId
        bankTransaction.account = account
        bankTransaction.date = LocalDate.now()
        bankTransaction.amount = BigDecimal.TEN
        Mockito.`when`(moneyBoxRepository!!.findByBankAccountId(bankAccountId)).thenReturn(Optional.of(moneyBox!!))
    }

    @Test
    fun doToMovement_allNoteAttributesHaveContent_validStringIsJoined() {
        bankTransaction.message = "msg1"
        bankTransaction.userIdentification = "payment 1234"
        bankTransaction.correspondingAccountName = "Company ABC"
        val movement = mapper!!.doToMovement(bankTransaction, user!!)
        Assertions.assertThat(movement!!.note).isEqualTo("msg1 | payment 1234 | Company ABC")
    }

    @Test
    fun doToMovement_oneNoteAttributesHaveContent_validStringIsJoined() {
        bankTransaction.userIdentification = "payment 1234"
        val movement = mapper!!.doToMovement(bankTransaction, user!!)
        Assertions.assertThat(movement!!.note).isEqualTo("payment 1234")
    }

    @Test
    fun doToMovement_allNoteAttributesAreNull_noteIsNull() {
        val movement = mapper!!.doToMovement(bankTransaction, user!!)
        Assertions.assertThat(movement!!.note).isEqualTo(null)
    }
}