package tech.corefinance.common.converter;

import org.springframework.core.convert.converter.Converter;

public interface ControllerOutputConverter {
    interface ListOutputConverter<S, T> extends Converter<S, T> {
    }

    interface DetailsOutputConverter<S, T> extends Converter<S, T> {
    }
}
