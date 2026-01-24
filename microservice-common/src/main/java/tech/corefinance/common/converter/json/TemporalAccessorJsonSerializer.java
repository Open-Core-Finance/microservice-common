package tech.corefinance.common.converter.json;

import com.fasterxml.jackson.databind.JavaType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.Version;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Iterator;

@Data
@Order
@EqualsAndHashCode(callSuper = false)
@Slf4j
public abstract class TemporalAccessorJsonSerializer<T extends TemporalAccessor> extends ValueSerializer<T> implements ModelConverter {

    private final String dateTimeFormat;
    private final JsonMapper jsonMapper;
    private final DateTimeFormatter dateTimeFormatter;
    private final SpringDocProviders springDocProviders;

    public TemporalAccessorJsonSerializer(@NotNull String dateTimeFormat, @Autowired JsonMapper jsonMapper,
                                          @Autowired SpringDocProviders springDocProviders) {
        this.jsonMapper = jsonMapper;
        this.dateTimeFormat = dateTimeFormat;
        this.springDocProviders = springDocProviders;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
    }

    @PostConstruct
    protected void postConstruct() {
        assert dateTimeFormat != null : "DateTimeFormat cannot be null";
        var handleClassName = handledType().getSimpleName();
        log.debug("Applied {} format: {}", handleClassName, dateTimeFormat);
        Version version = new Version(1, 0, 0, null, "", "");
        SimpleModule simpleModule = new SimpleModule(handleClassName + "Module", version);
        simpleModule.addSerializer(this);
        // jsonMapper.registeredModules().add(simpleModule);

        var tmp = new com.fasterxml.jackson.databind.module.SimpleModule(simpleModule.getModuleName(),
                new com.fasterxml.jackson.core.Version(version.getMajorVersion(), version.getMinorVersion(),
                        version.getPatchLevel(), null, version.getGroupId(), version.getGroupId()));
        springDocProviders.jsonMapper().registerModule(tmp);
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
        log.debug("Converting value [{}]", value);
        gen.writeString(dateTimeFormatter.format(value));
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

    @Override
    public abstract Class<T> handledType();
}
