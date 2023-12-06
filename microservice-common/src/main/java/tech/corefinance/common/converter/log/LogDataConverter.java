package tech.corefinance.common.converter.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;

public abstract class LogDataConverter extends ClassicConverter {

    /**
     * Output string format.
     */
    private String outputFormat;
    private boolean returnEmptyIfDataBlank;
    private String label;

    protected LogDataConverter(String outputFormat, boolean returnEmptyIfDataBlank, String label) {
        this.outputFormat = outputFormat;
        this.returnEmptyIfDataBlank = returnEmptyIfDataBlank;
        this.label = label;
    }

    protected LogDataConverter(String label) {
        this("[%label%: %data%]", true, label);
    }

    @Override
    public String convert(ILoggingEvent event) {
        String data = retrieveData();
        if (returnEmptyIfDataBlank && StringUtils.isBlank(data)) {
            return "";
        }
        return outputFormat.replace("%label%", label).replace("%data%", StringUtils.trimToEmpty(data));
    }

    protected abstract String retrieveData();

}
