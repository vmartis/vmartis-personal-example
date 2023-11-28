package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.bank.dto.BankAccountDto
import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import cz.smartbrains.qesu.module.bank.repository.BankAccountRepository
import cz.smartbrains.qesu.module.bank.service.BankAccountService
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyMovementRepository
import cz.smartbrains.qesu.module.subject.entity.Subject
import cz.smartbrains.qesu.module.subject.repository.SubjectRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class MoneyMovementSubjectPairServiceImplTest {
    @Mock
    private val subjectRepository: SubjectRepository? = null

    @Mock
    private val bankAccountService: BankAccountService? = null

    @Mock
    private val bankMoneyMovementRepository: BankMoneyMovementRepository? = null

    @Mock
    private val bankAccountRepository: BankAccountRepository? = null

    @Captor
    private val subjectArgumentCaptor: ArgumentCaptor<Subject>? = null
    private var service: MoneyMovementSubjectPairService? = null

    @BeforeEach
    fun setUp() {
        service = MoneyMovementSubjectPairServiceImpl(subjectRepository!!, bankAccountService!!, bankAccountRepository!!,
                bankMoneyMovementRepository!!)
    }

    @Test
    fun pairSubject_bankMoneyMovementWthSubject_directSubjectPair() {
        val movementId = 1L
        val bankAccountId = 3L
        val bankTransactionId = 4L
        val subjectId = 5L
        val accountId = "123456789"
        val bankId = "2010"

        //mock persisted entity
        val bankTransactionEntity = Mockito.mock(BankTransaction::class.java)
        val createdNewBankAccountEntity = Mockito.mock(BankAccount::class.java)
        val movementEntity = Mockito.mock(BankMoneyMovement::class.java)
        Mockito.`when`(movementEntity.bankTransaction).thenReturn(bankTransactionEntity)
        Mockito.`when`(bankMoneyMovementRepository!!.findById(movementId)).thenReturn(Optional.of(movementEntity))
        val subjectEntity = Mockito.mock(Subject::class.java)
        Mockito.`when`(subjectEntity.id).thenReturn(subjectId)
        Mockito.`when`(subjectRepository!!.findById(subjectId)).thenReturn(Optional.of(subjectEntity))
        Mockito.`when`(bankTransactionEntity.id).thenReturn(bankTransactionId)
        Mockito.`when`(bankTransactionEntity.correspondingAccountNumber).thenReturn(accountId)
        Mockito.`when`(createdNewBankAccountEntity.accountId).thenReturn(accountId)
        Mockito.`when`(createdNewBankAccountEntity.bankId).thenReturn(bankId)
        val movementUnpairedEntity = Mockito.mock(BankMoneyMovement::class.java)
        val bankAccountDto = Mockito.mock(BankAccountDto::class.java)
        Mockito.`when`(bankAccountDto.id).thenReturn(bankAccountId)
        Mockito.`when`(bankAccountService!!.storeCorrespondingBankAccount(bankTransactionId, subjectId)).thenReturn(bankAccountDto)
        Mockito.`when`(bankAccountRepository!!.getById(bankAccountId)).thenReturn(createdNewBankAccountEntity)
        Mockito.`when`(bankMoneyMovementRepository.findUnpairedByAccountNumber(accountId, bankId, null)).thenReturn(listOf(movementUnpairedEntity))
        service!!.pairSubject(movementId, subjectId)
        Mockito.verify(bankAccountService).storeCorrespondingBankAccount(bankTransactionId, subjectId)
        Mockito.verify(bankMoneyMovementRepository).findUnpairedByAccountNumber(accountId, bankId, null)
        Mockito.verify(movementUnpairedEntity).correspondingBankAccount = createdNewBankAccountEntity
    }

    @Test
    fun pairSubject_bankMoneyMovementWthSubject_newCorrespondingAccountCreated() {
        val movementId = 1L
        val subjectId = 5L

        //mock persisted entity
        val bankTransactionEntity = Mockito.mock(BankTransaction::class.java)
        val movementEntity = Mockito.mock(BankMoneyMovement::class.java)
        Mockito.`when`(movementEntity.bankTransaction).thenReturn(bankTransactionEntity)
        Mockito.`when`(bankMoneyMovementRepository!!.findById(movementId)).thenReturn(Optional.of(movementEntity))
        val subjectEntity = Mockito.mock(Subject::class.java)
        Mockito.`when`(subjectEntity.id).thenReturn(subjectId)
        Mockito.`when`(subjectRepository!!.findById(subjectId)).thenReturn(Optional.of(subjectEntity))
        service!!.pairSubject(movementId, subjectId)
        Mockito.verify(movementEntity).subject = subjectArgumentCaptor!!.capture()
        Assertions.assertThat(subjectArgumentCaptor.value).isSameAs(subjectEntity)
    }

    @Test
    fun pairSubject_changeSubject_correspondingAccountAndAllMovementsSubjectChanged() {
        val movementId = 1L
        val bankAccountId = 3L
        val oldSubjectId = 5L
        val newSubjectId = 6L
        val oldSubjectEntity = Mockito.mock(Subject::class.java)
        Mockito.`when`(oldSubjectEntity.id).thenReturn(oldSubjectId)
        val newSubjectEntity = Mockito.mock(Subject::class.java)
        Mockito.`when`(newSubjectEntity.id).thenReturn(newSubjectId)
        Mockito.`when`(subjectRepository!!.findById(newSubjectId)).thenReturn(Optional.of(newSubjectEntity))

        //mock persisted entity
        val movementEntity = Mockito.mock(BankMoneyMovement::class.java)
        Mockito.`when`(movementEntity.subject).thenReturn(oldSubjectEntity)
        Mockito.`when`(bankMoneyMovementRepository!!.findById(movementId)).thenReturn(Optional.of(movementEntity))
        val correspondingBankAccount = Mockito.mock(BankAccount::class.java)
        Mockito.`when`(correspondingBankAccount.id).thenReturn(bankAccountId)
        Mockito.`when`(bankAccountRepository!!.getById(bankAccountId)).thenReturn(correspondingBankAccount)
        Mockito.`when`(movementEntity.correspondingBankAccount).thenReturn(correspondingBankAccount)
        service!!.pairSubject(movementId, newSubjectId)
        Mockito.verify(correspondingBankAccount).subject = newSubjectEntity
        Mockito.verify(bankMoneyMovementRepository).updateSubjectForCorrespondingAccount(bankAccountId, newSubjectEntity)
    }

    @Test
    fun pairSubject_changeSubjectOfMovementWithoutCorrespondingAccount_subjectChanged() {
        val movementId = 1L
        val oldSubjectId = 5L
        val newSubjectId = 6L
        val oldSubjectEntity = Mockito.mock(Subject::class.java)
        Mockito.`when`(oldSubjectEntity.id).thenReturn(oldSubjectId)
        val newSubjectEntity = Mockito.mock(Subject::class.java)
        Mockito.`when`(newSubjectEntity.id).thenReturn(newSubjectId)
        Mockito.`when`(subjectRepository!!.findById(newSubjectId)).thenReturn(Optional.of(newSubjectEntity))

        //mock persisted entity
        val movementEntity = Mockito.mock(BankMoneyMovement::class.java)
        Mockito.`when`(movementEntity.subject).thenReturn(oldSubjectEntity)
        Mockito.`when`(bankMoneyMovementRepository!!.findById(movementId)).thenReturn(Optional.of(movementEntity))
        service!!.pairSubject(movementId, newSubjectId)
        Mockito.verify(movementEntity).subject = newSubjectEntity
    }

    @Test
    fun pairSubject_unpairSubject_correspondingAccountAndAllMovementsSubjectUnpaired() {
        val movementId = 1L
        val bankAccountId = 3L
        val oldSubjectEntity = Mockito.mock(Subject::class.java)

        //mock persisted entity
        val movementEntity = Mockito.mock(BankMoneyMovement::class.java)
        Mockito.`when`(movementEntity.subject).thenReturn(oldSubjectEntity)
        Mockito.`when`(bankMoneyMovementRepository!!.findById(movementId)).thenReturn(Optional.of(movementEntity))
        val correspondingBankAccount = Mockito.mock(BankAccount::class.java)
        Mockito.`when`(correspondingBankAccount.id).thenReturn(bankAccountId)
        Mockito.`when`(bankAccountRepository!!.getById(bankAccountId)).thenReturn(correspondingBankAccount)
        Mockito.`when`(movementEntity.correspondingBankAccount).thenReturn(correspondingBankAccount)
        service!!.pairSubject(movementId, null)
        Mockito.verify(movementEntity).subject = null
        Mockito.verify(movementEntity).correspondingBankAccount = null
        Mockito.verify(bankMoneyMovementRepository).unpairBankAccount(bankAccountId)
        Mockito.verify(bankAccountRepository).deleteById(bankAccountId)
    }

    @Test
    fun pairSubject_unpairSubjectOfMovementWithoutCorrespondingAccount_subjectChanged() {
        val movementId = 1L
        val oldSubjectEntity = Mockito.mock(Subject::class.java)

        //mock persisted entity
        val movementEntity = Mockito.mock(BankMoneyMovement::class.java)
        Mockito.`when`(movementEntity.subject).thenReturn(oldSubjectEntity)
        Mockito.`when`(bankMoneyMovementRepository!!.findById(movementId)).thenReturn(Optional.of(movementEntity))
        service!!.pairSubject(movementId, null)
        Mockito.verify(movementEntity).subject = null
    }
}