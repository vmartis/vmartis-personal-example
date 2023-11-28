package cz.smartbrains.qesu.module.common.service

import com.google.common.base.Preconditions
import cz.smartbrains.qesu.module.common.entity.Orderable
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.repository.OrderableEntityRepository
import org.springframework.stereotype.Service

@Service
class OrderableEntityService {
    fun <E : Orderable> updatePosition(repository: OrderableEntityRepository<E>, entityId: Long, position: Int): E {
        Preconditions.checkNotNull(entityId)
        Preconditions.checkArgument(position >= 0)
        val entity = repository.findById(entityId).orElseThrow { RecordNotFoundException() }
        if (position > entity!!.order) {
            repository.decrementOrderRange(entity.order + 1, position)
        } else if (position < entity.order) {
            repository.incrementOrderRange(position, entity.order - 1)
        }
        entity.order = position
        return repository.save(entity)
    }

    fun <E : Orderable> newItemOrder(repository: OrderableEntityRepository<E>): Int {
        return repository.count().toInt() + 1
    }
}