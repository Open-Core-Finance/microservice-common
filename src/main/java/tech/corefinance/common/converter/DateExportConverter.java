package tech.corefinance.common.converter;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import tech.corefinance.common.ex.ServiceProcessingException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DateExportConverter implements ExportTypeConverter<Object, String> {

    @Value("${export.format.date:dd/MM/yyyy}")
    private String exportDateFormat;
    @Value("${export.format.datetime:dd/MM/yyyy HH:mm:ss}")
    private String exportDateTimeFormat;

    private SimpleDateFormat dateFormat;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter dateTimeFormatter;
    private DateTimeFormatter instantFormatter;

    @PostConstruct
    public void postConstruct() {
        dateFormat = new SimpleDateFormat(exportDateFormat);
        dateFormatter = DateTimeFormatter.ofPattern(exportDateFormat);
        dateTimeFormatter = DateTimeFormatter.ofPattern(exportDateTimeFormat);
        instantFormatter = DateTimeFormatter.ofPattern(exportDateTimeFormat).withZone(ZoneId.systemDefault());
    }

    @Override
    public String convert(Object data) {
        if (data != null) {
            Class<?> clzz = data.getClass();
            if (Date.class.isAssignableFrom(clzz)) {
                return dateFormat.format((Date) data);
            }
            if (ChronoLocalDate.class.isAssignableFrom(clzz)) {
                return dateFormatter.format((ChronoLocalDate) data);
            }
            if (ChronoLocalDateTime.class.isAssignableFrom(clzz)) {
                return dateTimeFormatter.format((ChronoLocalDateTime<?>) data);
            }
            if (ChronoZonedDateTime.class.isAssignableFrom(clzz)) {
                return dateTimeFormatter.format((ChronoZonedDateTime<?>) data);
            }
            if (Instant.class.isAssignableFrom(clzz)) {
                return instantFormatter.format((Instant) data);
            }
            throw new ServiceProcessingException(
                    "Cannot convert type " + clzz.getName() + " to " + String.class.getName());
        } else {
            return "";
        }
    }

    @Override
    public boolean isSupport(Class<?> dataType) {
        Class<?>[] supportedType =
                new Class[]{Date.class, ChronoLocalDate.class, ChronoLocalDateTime.class, ChronoZonedDateTime.class,
                        Instant.class};
        for (Class<?> clzz : supportedType) {
            if (clzz.isAssignableFrom(dataType)) {
                return true;
            }
        }
        return false;
    }

}
