package com.epam.training.ticketservice.core.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class LocalDateTime2StringConverter implements Converter<LocalDateTime, String> {
 
    private final DateTimeFormatter formatter;
 
    public LocalDateTime2StringConverter() {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }
 
    @Override
    public String convert(LocalDateTime source) {
        if (source == null) {
            return null;
        }
 
        return formatter.format(source);
    }
}
