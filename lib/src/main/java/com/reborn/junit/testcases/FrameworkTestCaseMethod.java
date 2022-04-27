package com.reborn.junit.testcases;

import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runners.model.FrameworkMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class FrameworkTestCaseMethod extends FrameworkMethod {
    private final TestCase _testCase;

    private final Map<Class<?>, Function<String, Object>> PARAMETER_CONVERTERS = new HashMap<>();

    public FrameworkTestCaseMethod(final Method method,
                                   final TestCase testCase) {
        super(method);
        _testCase = testCase;
        PARAMETER_CONVERTERS.put(String.class, x -> x);
        PARAMETER_CONVERTERS.put(int.class, Integer::parseInt);
    }

    private Object[] getParameters() {
        final Parameter[] parameters = getMethod().getParameters();
        final String[] arguments = _testCase.value();

        final Object[] finalParams = new Object[arguments.length];

        for (int i = 0; i < parameters.length; i++) {
            final Class<?> parameterType = parameters[i].getType();
            final String argument = arguments[i];
            finalParams[i] = PARAMETER_CONVERTERS.get(parameterType).apply(argument);
        }
        return finalParams;
    }

    @Override
    public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
        if(params.length != 0)
            throw new Exception();

        return new ReflectiveCallable() {
            @Override
            protected Object runReflectiveCall() throws Throwable {
                return getMethod().invoke(target, getParameters());
            }
        }.run();
    }

    @Override
    public String getName() {
        return getMethod().getName() + "(" + String.join(",", _testCase.value()) + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FrameworkTestCaseMethod that = (FrameworkTestCaseMethod) o;
        return _testCase.equals(that._testCase) && getMethod().equals(that.getMethod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), _testCase);
    }
}