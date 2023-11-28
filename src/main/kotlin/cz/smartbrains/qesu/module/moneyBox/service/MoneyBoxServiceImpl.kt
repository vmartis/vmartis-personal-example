package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxBalanceDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBox
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance
import cz.smartbrains.qesu.module.moneyBox.mapper.MoneyBoxBalanceMapper
import cz.smartbrains.qesu.module.moneyBox.mapper.MoneyBoxMapper
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyMovementRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.function.Consumer
import java.util.stream.Collectors

@Service
class MoneyBoxServiceImpl(private val moneyMovementRepository: MoneyMovementRepository,
                          private val moneyBoxRepository: MoneyBoxRepository,
                          private val orderableEntityService: OrderableEntityService,
                          private val moneyBoxMapper: MoneyBoxMapper,
                          private val moneyBoxBalanceMapper: MoneyBoxBalanceMapper) : MoneyBoxService {
    @Transactional(readOnly = true)
    override fun find(id: Long): MoneyBoxDto {
        return moneyBoxMapper.doToDto(moneyBoxRepository.findById(id).orElseThrow { RecordNotFoundException() })!!
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<MoneyBoxDto> {
        return moneyBoxRepository.findAll(Sort.by(Sort.Direction.ASC, "order"))
                .stream()
                .map { moneyBox: MoneyBox -> moneyBoxMapper.doToDto(moneyBox)!! }
                .collect(Collectors.toList())
    }

    @Transactional(readOnly = true)
    override fun balances(forDate: LocalDate, currency: String): List<MoneyBoxBalanceDto> {
        val balances: MutableList<MoneyBoxBalanceDto> = moneyMovementRepository.computeBalancesForDate(forDate, currency)
                .stream()
                .map { moneyBoxBalance: MoneyBoxBalance? -> moneyBoxBalanceMapper.doToDto(moneyBoxBalance) }
                .collect(Collectors.toList())
        val moneyBoxes = moneyBoxRepository.findAllActiveByCurrency(currency)

        // if not all balances are present, ensure add missing balance for non-having movements with totalAmount equals to initial amount of money box
        if (moneyBoxes.size > balances.size) {
            moneyBoxes.forEach(Consumer { moneyBox: MoneyBox ->
                val balanceForMoneyBox = balances.stream()
                        .filter { balance: MoneyBoxBalanceDto -> balance.moneyBox!!.id == moneyBox.id }
                        .findFirst()
                // od it only if balance doesn't exist
                if (balanceForMoneyBox.isEmpty) {
                    balances.add(MoneyBoxBalanceDto(moneyBox.getInitialAmount(forDate), forDate, moneyBoxMapper.doToDto(moneyBox)))
                }
            })
        }
        return balances
    }

    @Transactional
    override fun updatePosition(moneyBoxId: Long, position: Int): MoneyBoxDto {
        val moneyBox = orderableEntityService.updatePosition(moneyBoxRepository, moneyBoxId, position)
        return moneyBoxMapper.doToDto(moneyBox)!!
    }
}