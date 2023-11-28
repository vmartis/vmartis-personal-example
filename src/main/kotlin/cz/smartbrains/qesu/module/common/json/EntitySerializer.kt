package cz.smartbrains.qesu.module.common.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.type.WritableTypeId
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import cz.smartbrains.qesu.logger
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import lombok.extern.slf4j.Slf4j
import java.io.IOException


@Slf4j
open class EntitySerializer<T : AbstractDto> : JsonSerializer<T?>() {
    val logger = logger()

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serializeWithType(
        value: T?, gen: JsonGenerator?,
        provider: SerializerProvider?, typeSer: TypeSerializer,
    ) {
        if (value != null) {
            val typeId: WritableTypeId = typeSer.typeId(value, JsonToken.START_OBJECT)
            typeSer.writeTypePrefix(gen, typeId)
            gen!!.writeNumberField("id", value.id!!)
            typeSer.writeTypeSuffix(gen, typeId)
        }
    }

    override fun serialize(value: T?, gen: JsonGenerator, serializers: SerializerProvider?) {
        logger.info("EntitySerializer: {}", value)
        if (value != null) {
            gen.writeStartObject()
            gen.writeNumberField("id", value.id!!)
            gen.writeEndObject()
        }
    }
}