package cz.smartbrains.qesu.jaxb

import org.apache.commons.lang3.StringUtils
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.adapters.XmlAdapter

class LocalDateTimeAdapter : XmlAdapter<String?, LocalDateTime?>() {
    override fun unmarshal(dateTime: String?): LocalDateTime? {
        return if (StringUtils.isNotBlank(dateTime)) {
            OffsetDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime()
        } else {
            null
        }
    }

    override fun marshal(dateTime: LocalDateTime?): String? {
        return dateTime?.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}