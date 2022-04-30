package com.reborn.junit.testcases;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ParameterConverter {
    private static final SimpleDateFormat ISO_DATE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public Object tryConvert(final String argument, final Class<?> targetClass) throws Exception {
        if(argument.equalsIgnoreCase("null"))
            return null;
        if(Boolean.class == targetClass || boolean.class == targetClass) return Boolean.parseBoolean(argument);
        if(Byte.class == targetClass || byte.class == targetClass) return Byte.parseByte(argument);
        if(Short.class == targetClass || short.class == targetClass) return Short.parseShort(argument);
        if(Integer.class == targetClass || int.class == targetClass) return Integer.parseInt(argument);
        if(Long.class == targetClass || long.class == targetClass) return Long.parseLong(argument);
        if(Float.class == targetClass || float.class == targetClass) return Float.parseFloat(argument);
        if(Double.class == targetClass || double.class == targetClass) return Double.parseDouble(argument);
        if(String.class == targetClass) return argument;

        if(Date.class == targetClass) return ISO_DATE.parse(argument);
        if(LocalDateTime.class == targetClass) return LocalDateTime.parse(argument, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if(LocalDate.class == targetClass) return LocalDate.parse(argument, DateTimeFormatter.ISO_LOCAL_DATE);
        if(LocalTime.class == targetClass) return LocalTime.parse(argument, DateTimeFormatter.ISO_LOCAL_TIME);
        if(Instant.class == targetClass) return Instant.parse(argument);

        throw new Exception("We cannot handle conversions to " + targetClass + " sorry :(");
    }
}

