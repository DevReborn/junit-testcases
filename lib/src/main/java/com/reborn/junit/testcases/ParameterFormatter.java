package com.reborn.junit.testcases;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class ParameterFormatter {
    public String format(final String argument, final Class<?> targetClass) {
        if (argument.equalsIgnoreCase("null"))
            return argument;
        if (Boolean.class == targetClass || boolean.class == targetClass) return argument;
        if (Byte.class == targetClass || byte.class == targetClass) return argument;
        if (Short.class == targetClass || short.class == targetClass) return argument;
        if (Integer.class == targetClass || int.class == targetClass) return argument;
        if (Long.class == targetClass || long.class == targetClass) return argument;
        if (Float.class == targetClass || float.class == targetClass) return argument;
        if (Double.class == targetClass || double.class == targetClass) return argument;
        if (String.class == targetClass)
            return "\"" + argument.replaceAll("\"", "\\\"") + "\"";

        if (Date.class == targetClass
            || LocalDateTime.class == targetClass
            || LocalDate.class == targetClass
            || LocalTime.class == targetClass
            || Instant.class == targetClass)
            return "\"" + argument + "\"";

        return argument;
    }
}
