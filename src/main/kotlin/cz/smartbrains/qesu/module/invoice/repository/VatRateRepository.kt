package cz.smartbrains.qesu.module.invoice.repository

import cz.smartbrains.qesu.module.common.repository.CodeListRepository
import cz.smartbrains.qesu.module.invoice.entity.VatRate

interface VatRateRepository : CodeListRepository<VatRate>