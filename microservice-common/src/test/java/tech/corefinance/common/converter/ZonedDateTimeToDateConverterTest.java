package tech.corefinance.common.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ZonedDateTimeToDateConverterTest {

    private ZonedDateTimeToDateConverter zonedDateTimeToDateConverter;

    @BeforeEach
    public void setup() {
        zonedDateTimeToDateConverter = new ZonedDateTimeToDateConverter();
    }

    @Test
    public void test_convert_null() {
        assertNull(zonedDateTimeToDateConverter.convert(null, null, null));
    }

    @Test
    public void test_convert_normal() {
        var input = ZonedDateTime.now();
        var output = zonedDateTimeToDateConverter.convert(input, null, null);
        assertNotNull(output);
        assertEquals(input.toInstant().toEpochMilli(), ((Date) output).getTime());
    }
}
