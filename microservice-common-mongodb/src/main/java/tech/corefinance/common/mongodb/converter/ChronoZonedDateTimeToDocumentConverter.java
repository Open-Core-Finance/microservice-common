package tech.corefinance.common.mongodb.converter;

import java.time.chrono.ChronoZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bson.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Converter convert ChronoZonedDateTime like ZonedDateTime to MongoDB Document.
 */
@Component
@ConditionalOnProperty(name = "tech.corefinance.common-mongodb.converter.zoned-date-time", havingValue = "true", matchIfMissing = true)
@WritingConverter
public class ChronoZonedDateTimeToDocumentConverter
        implements MongoConversionSupport<ChronoZonedDateTime<?>, Document>, GenericConverter {

    static final String DATE_TIME = "dateTime";
    static final String ZONE = "zone";

    @Override
    public Document convert(@Nullable ChronoZonedDateTime<?> zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }

        Document document = new Document();
        document.put(DATE_TIME, Date.from(zonedDateTime.toInstant()));
        document.put(ZONE, zonedDateTime.getZone().getId());
        document.put("offset", zonedDateTime.getOffset().toString());
        return document;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<ConvertiblePair>();
        result.add(new ConvertiblePair(ChronoZonedDateTime.class, Document.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((ChronoZonedDateTime) source);
    }
}
