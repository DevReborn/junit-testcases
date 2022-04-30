package com.reborn.junit.testcases;

import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runners.model.FrameworkMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FrameworkTestCaseMethod extends FrameworkMethod {
    private static final ParameterConverter CONVERTER = new ParameterConverter();
    private static final ParameterFormatter FORMATTER = new ParameterFormatter();

    private final List<Pair> _parameters = new ArrayList<>();

    private final TestCase _testCase;

    public FrameworkTestCaseMethod(final Method method,
                                   final TestCase testCase) {
        super(method);
        _testCase = testCase;

        final Parameter[] parameters = getMethod().getParameters();
        final String[] arguments = _testCase.value();

        for (int i = 0; i < parameters.length; i++) {
            final Parameter parameterType = parameters[i];
            final String argument = arguments[i];
            _parameters.add(new Pair(parameterType, argument));
        }
    }


    public Object[] getParameters() throws Exception {
        final ArrayList<Object> finalParams = new ArrayList<>();
        for (final Pair par : _parameters) {
            finalParams.add(CONVERTER.tryConvert(par.argument, par.parameterType.getType()));
        }
        return finalParams.toArray();
    }

    @Override
    public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
        if(params.length != 0)
            throw new Exception("Parameters were introduced from somewhere else. I dunno which ones to choose?!?!");

        return new ReflectiveCallable() {
            @Override
            protected Object runReflectiveCall() throws Throwable {
                return getMethod().invoke(target, getParameters());
            }
        }.run();
    }

    @Override
    public String getName() {
        return getMethod().getName() + "(" + _parameters
                .stream()
                .map(x -> FORMATTER.format(x.argument, x.parameterType.getType()))
                .collect(Collectors.joining(", "))
                + ")";
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

    private static class Pair {
        public Pair(final Parameter parameterType, final String argument) {

            this.parameterType = parameterType;
            this.argument = argument;
        }

        public final Parameter parameterType;
        public final String argument;
    }
}
