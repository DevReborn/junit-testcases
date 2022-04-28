package com.reborn.junit.testcases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

class ParameterConverterTests {

    private static class TypeProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of("1", Integer.class, 1),
                Arguments.of("true", Boolean.class, true),
                Arguments.of("123", Byte.class, (byte)123),
                Arguments.of("4557", Short.class, (short)4557),
                Arguments.of("45547245", Long.class, 45547245L),
                Arguments.of("34475.2356", Float.class, 34475.2356f),
                Arguments.of("34475.2356", Double.class, 34475.2356d),
                Arguments.of("HEY!", String.class, "HEY!"),
                Arguments.of("2020-01-01T10:10:10", Date.class, new Date(2020 - 1900, Calendar.JANUARY, 1, 10, 10, 10)),
                Arguments.of("2020-01-01T10:10:10", LocalDateTime.class, LocalDateTime.of(2020, 1, 1, 10, 10, 10)),
                Arguments.of("2020-01-01", LocalDate.class, LocalDate.of(2020, 1, 1)),
                Arguments.of("10:10:10", LocalTime.class, LocalTime.of(10, 10, 10)),
                Arguments.of("2020-01-01T10:10:10Z", Instant.class, Instant.parse("2020-01-01T10:10:10Z"))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(TypeProvider.class)
    public void tryConvert(final String text, final Class<?> klass, final Object expected) throws Exception {
        // Given
        final ParameterConverter parameterConverter = new ParameterConverter();

        // When
        final Object result = parameterConverter.tryConvert(text, klass);

        // Then
        Assertions.assertEquals(expected, result);
    }
}