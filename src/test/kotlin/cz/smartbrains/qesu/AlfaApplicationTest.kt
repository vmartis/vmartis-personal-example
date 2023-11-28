package cz.smartbrains.qesu

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.EnabledIf

@SpringBootTest
@EnabledIf(expression = "#{'true'.equals(systemProperties['test.integration.enabled'])}")
class AlfaApplicationTest {
    @Test
    fun contextLoads() {
    }
}