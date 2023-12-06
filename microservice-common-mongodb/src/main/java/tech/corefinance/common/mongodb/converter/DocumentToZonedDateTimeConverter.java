package tech.corefinance.common.mongodb.converter;

import org.bson.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "tech.corefinance.common-mongodb.converter.zoned-date-time", havingValue = "true", matchIfMissing = true)
@ReadingConverter
public class DocumentToZonedDateTimeConverter
        implements MongoConversionSupport<Document, ZonedDateTime>, GenericConverter {

    @Override
    public ZonedDateTime convert(@Nullable Document document) {
        if (document == null) {
            return null;
        }

        Date dateTime = document.getDate(ChronoZonedDateTimeToDocumentConverter.DATE_TIME);
        String zoneId = document.getString(ChronoZonedDateTimeToDocumentConverter.ZONE);
        ZoneId zone = ZoneId.of(zoneId);

        return ZonedDateTime.ofInstant(dateTime.toInstant(), zone);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        var result = new HashSet<ConvertiblePair>();
        result.add(new ConvertiblePair(Document.class, ChronoZonedDateTime.class));
        return result;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((Document) source);
    }
}
