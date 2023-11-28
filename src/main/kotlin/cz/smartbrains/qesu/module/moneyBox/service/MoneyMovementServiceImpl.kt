package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementFilter
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement
import cz.smartbrains.qesu.module.moneyBox.mapper.MoneyMovementMapper
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyMovementRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class MoneyMovementServiceImpl(private val moneyMovementRepository: MoneyMovementRepository,
                               private val moneyMovementMapper: MoneyMovementMapper) : MoneyMovementService {
    @Transactional(readOnly = true)
    override fun findByFilter(filter: MoneyMovementFilter): List<MoneyMovementDto> {
        return moneyMovementRepository.findByFilter(filter)
                .stream()
                .map { moneyMovement: MoneyMovement -> moneyMovementMapper.doToDto(moneyMovement)!! }
                .collect(Collectors.toList())
    }
}