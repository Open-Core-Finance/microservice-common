package tech.corefinance.common.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.corefinance.common.dto.CsvExportDefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvFieldListConverterTest {

    private CsvFieldListConverter csvFieldListConverter;

    @BeforeEach
    public void setUp() {
        csvFieldListConverter = new CsvFieldListConverter();
    }

    @Test
    public void test_parse_normal() {
        var input = "[{\"title\": \"Product name\", \"fieldName\": \"name\"}]";
        var outputArr = csvFieldListConverter.parse(input, null);
        assertEquals(1, outputArr.length);
        var output = outputArr[0];
        assertEquals("name", output.getFieldName());
    }

    @Test
    public void test_parse_null() {
        assertThrows(IllegalArgumentException.class, () -> csvFieldListConverter.parse(null, null));
    }

    @Test
    public void test_print() {
        var input = new CsvExportDefinition("Description", "description");
        var output = csvFieldListConverter.print(new CsvExportDefinition[]{input}, null);
        assertEquals("[{\"title\":\"Description\",\"fieldName\":\"description\"}]", output);
    }
}
