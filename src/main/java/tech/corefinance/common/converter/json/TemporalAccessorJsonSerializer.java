package tech.corefinance.common.converter.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.providers.SpringDocProviders;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Iterator;

@Data
@Order
@EqualsAndHashCode(callSuper=false)
@Slf4j
public abstract class TemporalAccessorJsonSerializer<T extends TemporalAccessor> extends JsonSerializer<T> implements
        InitializingBean, ModelConverter {

    private String dateTimeFormat;
    @Autowired
    private ObjectMapper objectMapper;
    private DateTimeFormatter dateTimeFormatter;
    @Autowired
    private SpringDocProviders springDocProviders;

    public TemporalAccessorJsonSerializer(@NotNull String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    @PostConstruct
    protected void postConstruct() {
        assert dateTimeFormat != null : "DateTimeFormat cannot be null";
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        log.debug("Converting value [{}]", value);
        gen.writeString(dateTimeFormatter.format(value));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        var handleClassName = handledType().getSimpleName();
        log.debug("Applied {} format: {}", handleClassName, dateTimeFormat);
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        SimpleModule simpleModule = new SimpleModule(handleClassName + "Module",
                new Version(1, 0, 0, null, "", ""));
        simpleModule.addSerializer(this);
        objectMapper.registerModule(simpleModule);
        springDocProviders.jsonMapper().registerModule(simpleModule);
    }

    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        var handledType = handledType();
        JavaType javaType = springDocProviders.jsonMapper().constructType(type.getType());
        Class<?> annotatedTypeClass = javaType.getRawClass();
        var isAssignable = handledType.isAssignableFrom(annotatedTypeClass);
        log.debug("handledType [{}], annotatedTypeClass [{}], isAssignable [{}]", handledType, annotatedTypeClass,
                isAssignable);
        if (isAssignable) {
            return new TemporalAccessorSchema<T>(dateTimeFormatter, handledType, dateTimeFormat);
        }
        return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
    }
}
