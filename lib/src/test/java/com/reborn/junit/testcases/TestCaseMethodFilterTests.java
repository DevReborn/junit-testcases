package com.reborn.junit.testcases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

class TestCaseMethodFilterTests {

    private static class ArgumentProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("method1", TestClass.class, true, "method1(com.reborn.junit.testcases.TestClass)"),
                    Arguments.of("method1", TestClass.class, false, "method2(com.reborn.junit.testcases.TestClass)"),
                    Arguments.of("method2(\"hey\")", TestClass.class, true, "method2(com.reborn.junit.testcases.TestClass)"),
                    Arguments.of("method2(\"hey)\")", TestClass.class, true, "method2(com.reborn.junit.testcases.TestClass)"),
                    Arguments.of("method2(\"hey()\")", TestClass.class, true, "method2(com.reborn.junit.testcases.TestClass)"),
                    Arguments.of("method2(\"hey(()\")", TestClass.class, true, "method2(com.reborn.junit.testcases.TestClass)"),
                    Arguments.of("method2(\"(hey)(()\")", TestClass.class, true, "method2(com.reborn.junit.testcases.TestClass)"),
                    Arguments.of("method3(\"(hey)(()\", \"2020-01-01\")", TestClass.class, true, "method3(com.reborn.junit.testcases.TestClass)"),
                    Arguments.of("method4(\"(hey)(()\", \"2020-01-01\")", TestClass.GenericInnerClass.class, true, "method4(com.reborn.junit.testcases.TestClass$GenericInnerClass)")
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ArgumentProvider.class)
    void shouldRun(final String methodSig, final Class<?> klass, final boolean expected, final String filterName) throws NoSuchMethodException {
        // Given
        final TestCaseMethodFilter filter = new TestCaseMethodFilter(new Filter() {
            @Override
            public boolean shouldRun(final Description description) {
                return description.toString().equals(filterName);
            }

            @Override
            public String describe() {
                return "";
            }
        });

        // When
        final boolean result = filter.shouldRun(Description.createTestDescription(klass, methodSig));

        // Then
        assertEquals(expected, result);
    }


}

class TestClass {
    public void method1() {

    }
    public void method2(final String str) {

    }
    public void method3(final String str, final LocalDate date) {

    }

    class InnerClass {
        public void method4(final String str, final LocalDate date) {

        }
    }

    class GenericInnerClass<T, T2> {
        public void method4(final T str, final T2 date) {

        }
    }
}