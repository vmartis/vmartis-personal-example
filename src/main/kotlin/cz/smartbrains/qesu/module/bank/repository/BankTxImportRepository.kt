package cz.smartbrains.qesu.module.bank.repository

import cz.smartbrains.qesu.module.bank.entity.BankTxImport
import org.springframework.data.jpa.repository.JpaRepository

interface BankTxImportRepository : JpaRepository<BankTxImport, Long>