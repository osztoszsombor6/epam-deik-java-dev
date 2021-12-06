package com.epam.training.ticketservice.core.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
 
    private final DateTimeFormatter formatter;
 
    public LocalDateTimeConverter() {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }
 
    @Override
    public LocalDateTime convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
 
        return LocalDateTime.parse(source, formatter);
    }
}
