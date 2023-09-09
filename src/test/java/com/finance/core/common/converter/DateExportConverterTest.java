package com.finance.core.common.converter;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import com.finance.core.common.ex.ServiceProcessingException;
import com.finance.core.common.repository.ResourceActionRepository;
import com.finance.core.common.test.support.app.TestCommonApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestCommonApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"common", "default", "unittest"})
public class DateExportConverterTest {

    @Autowired
    private DateExportConverter converter;

    @Value("${export.format.date}")
    private String exportDateFormat;
    @Value("${export.format.datetime}")
    private String exportDateTimeFormat;

    private SimpleDateFormat dateFormat;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter dateTimeFormatter;
    private DateTimeFormatter instantFormatter;

    @MockBean
    private ResourceActionRepository resourceActionRepository;

    @BeforeEach
    public void setUp() {
        PowerMockito.when(resourceActionRepository.saveAll(Mockito.any())).thenReturn(new LinkedList<>());

        dateFormat = new SimpleDateFormat(exportDateFormat);
        dateFormatter = DateTimeFormatter.ofPattern(exportDateFormat);
        dateTimeFormatter = DateTimeFormatter.ofPattern(exportDateTimeFormat);
        instantFormatter = DateTimeFormatter.ofPattern(exportDateTimeFormat).withZone(ZoneId.systemDefault());
    }

    @Test
    void test_convert_null() {
        assertEquals("", converter.convert(null));
    }

    @Test
    void test_convert_ZonedDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        assertEquals(dateTimeFormatter.format(zonedDateTime), converter.convert(zonedDateTime));
    }

    @Test
    void test_convert_LocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        assertEquals(dateTimeFormatter.format(localDateTime), converter.convert(localDateTime));
    }

    @Test
    void test_convert_LocalDate() {
        LocalDate localDate = LocalDate.now();
        assertEquals(dateFormatter.format(localDate), converter.convert(localDate));
    }

    @Test
    void test_convert_Date() {
        Date date = new Date();
        assertEquals(dateFormat.format(date), converter.convert(date));
    }

    @Test
    void test_convert_Instant() {
        Instant instant = Instant.now();
        assertEquals(instantFormatter.format(instant), converter.convert(instant));
    }

    @Test
    void test_convert_InvalidType() {
        assertThrows(ServiceProcessingException.class, () -> converter.convert(10l));
    }
    
    @Test
    void test_isSupport_happyCase() {
        assertTrue(converter.isSupport(Date.class));
        assertTrue(converter.isSupport(LocalDate.class));
        assertTrue(converter.isSupport(LocalDateTime.class));
        assertTrue(converter.isSupport(Instant.class));
    }

    @Test
    void test_isSupport_unsupported() {
        assertFalse(converter.isSupport(Object.class));
        assertFalse(converter.isSupport(String.class));
        assertFalse(converter.isSupport(Calendar.class));
    }
}
