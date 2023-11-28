package cz.smartbrains.qesu.module.bank.mapper.camt

import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.bank.repository.BankAccountRepository
import org.assertj.core.api.Assertions
import org.iso.camt053.Document
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ResourceLoader
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.rules.SpringClassRule
import org.springframework.test.context.junit4.rules.SpringMethodRule
import java.io.IOException
import java.util.*
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.transform.stream.StreamSource

@SpringBootTest(classes = [BankTxImportCamtMapperImpl::class, BankTransactionCamtMapperImpl::class])
@ExtendWith(SpringExtension::class)
class CamtMapperRealFilesTest {
    @MockBean
    private val bankAccountRepository: BankAccountRepository? = null

    @Autowired
    private val bankTxImportCamtMapper: BankTxImportCamtMapper? = null

    @Autowired
    private val resourceLoader: ResourceLoader? = null

    @ParameterizedTest
    @ValueSource(strings = ["camt/exports/eur-acc-1.xml",
        "camt/exports/czk-acc-1.xml",
        "camt/exports/SK3709000000005158209077_1.xml",
        "camt/exports/SK3709000000005158209077_2.xml",
        "camt/exports/SK0811000000002923847110_TATRSKBXXXX-xxx-200303-000030343370.xml",
        "camt/exports/SK0811000000002923847110_TATRSKBXXXX-xxx-200304-000030359133.xml",
        "camt/exports/SK0811000000002923847110_TATRSKBXXXX-xxx-200305-000030374791.xml",
        "camt/exports/SK0811000000002923847110_TATRSKBXXXX-xxx-200305-000030374792.xml",
        "camt/exports/SK0811000000002923847110_TATRSKBXXXX-xxx-200312-000030462430.xml"])
    @Throws(JAXBException::class, IOException::class)
    fun test(filePath: String?) {
        val resource = resourceLoader!!.getResource("classpath:$filePath")
        val jaxbContext = JAXBContext.newInstance(Document::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()
        val document = unmarshaller.unmarshal(StreamSource(resource.inputStream), Document::class.java)
        Mockito.`when`(bankAccountRepository!!.findByIban(Mockito.anyString())).thenReturn(Optional.of(BankAccount()))
        val bankTxImports = bankTxImportCamtMapper!!.fromCamt(document.value, cz.smartbrains.qesu.module.document.entity.Document())
        Assertions.assertThat(bankTxImports).hasSize(1)
        Assertions.assertThat(bankTxImports[0].transactions).isNotEmpty
    }
}