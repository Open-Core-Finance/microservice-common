package tech.corefinance.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tech.corefinance.common.config.ExportingCsvConfig;
import tech.corefinance.common.config.ExportingEntityField;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ExportingServiceImpl implements ExportingService {
    private static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    static final String UTF8_BOM_UNICODE = "\uFEFF";
    private final Comparator<ExportingEntityField> exportingFieldComparator;
    private final CoreFinanceUtil coreFinanceUtil;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public ExportingServiceImpl(CoreFinanceUtil coreFinanceUtil, ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.coreFinanceUtil = coreFinanceUtil;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        exportingFieldComparator = (o1, o2) -> {
            if (o1 == null) {
                return o2 == null ? 0 : -o2.getIndex();
            } else {
                return (o2 == null) ? o1.getIndex() : o1.getIndex() - o2.getIndex();
            }
        };
    }

    public <T> void exportToUtf8Csv(List<T> entities, ExportingCsvConfig config, OutputStream output) {
        exportToCsvWithEncoding(entities, config, output, StandardCharsets.UTF_8, UTF8_BOM_UNICODE);
    }

    @Override
    public <T> void exportToCsvWithEncoding(List<T> entities, ExportingCsvConfig config, OutputStream output, Charset encoding,
            String bomVal) {
        var fields = config.getFields().stream().sorted(exportingFieldComparator).toList();
        OutputStreamWriter osw = new OutputStreamWriter(output, encoding);
        PrintWriter pw = new PrintWriter(new BufferedWriter(osw));
        if (StringUtils.hasText(bomVal)) {
            pw.print(bomVal);
        }
        if (config.isHeader()) {
            var index = 0;
            for (ExportingEntityField entityField : fields) {
                String val = "\"" + entityField.getLabel() + "\"";
                if (index > 0) {
                    pw.print(config.getDelimiter());
                }
                index++;
                pw.print(val);
            }
            pw.println();
        }
        // Print contents
        var index = 0;
        for (T entity : entities) {
            if (index > 0) {
                pw.println();
            }
            index++;
            printEntityToCsv(entity, pw, fields, config);
        }
        // Flush data
        pw.flush();
    }

    @Override
    public ExportingCsvConfig loadConfigFromPath(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return objectMapper.readValue(resource.getContentAsString(StandardCharsets.UTF_8), ExportingCsvConfig.class);
    }

    private <T> void printEntityToCsv(T entity, PrintWriter pw, List<ExportingEntityField> fields, ExportingCsvConfig config) {
        var index = 0;
        for (ExportingEntityField entityField : fields) {
            Object fieldVal = coreFinanceUtil.getDeepAttributeValue(entity, entityField.getField());
            if (index > 0) {
                pw.print(config.getDelimiter());
            }
            index++;
            if (fieldVal == null) {
                pw.print("NULL");
                continue;
            }
            String format = entityField.getFormat();
            if (StringUtils.hasText(format)) {
                pw.print("\"" + getValueAsFormat(fieldVal, format) + "\"");
            } else {
                pw.print("\"" + fieldVal + "\"");
            }
        }
    }

    private String getValueAsFormat(@NonNull Object value, @NonNull String format) {
        if (value instanceof String val) {
            return format.replace("{}", val);
        }
        if (value instanceof Number val) {
            DecimalFormat decimalFormat = new DecimalFormat(format);
            return decimalFormat.format(val);
        }
        if (value instanceof Boolean val) {
            var indexOfBool = format.indexOf("|");
            if (indexOfBool >= 0) {
                String trueVal = format.substring(0, indexOfBool);
                String falseVal = format.substring(indexOfBool + 1);
                return val ? trueVal : falseVal;
            }
        }

        if (value instanceof TemporalAccessor val) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            return dateTimeFormatter.format(val);
        }

        if (value instanceof Date val) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(val);
        }
        log.error("Unsupported format for value {}. Using toString instead", value);
        return value.toString();
    }
}
