package com.reborn.junit.testcases;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JUnit4WithTestCases extends BlockJUnit4ClassRunner {
    private static final ParameterConverter CONVERTER = new ParameterConverter();

    private final TestMethodEvaluator _testMethodEvaluator;

    public JUnit4WithTestCases(final Class<?> klass) throws InitializationError {
        super(klass);
        _testMethodEvaluator = new TestMethodEvaluator();
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        final List<FrameworkMethod> methods = super.getChildren();
        return _testMethodEvaluator.evaluateTestMethods(methods);
    }

    @Override
    public void filter(final Filter filter) throws NoTestsRemainException {
        super.filter(new TestCaseMethodFilter(filter));
    }

    @Override
    protected void validateTestMethods(final List<Throwable> errors) {
        final List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(Test.class);

        for (final FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoid(false, errors);
            validateTestCaseMethod(eachTestMethod, errors);
        }
    }

    private void validateTestCaseMethod(final FrameworkMethod method, final List<Throwable> errors) {
        final TestCases testCases = method.getAnnotation(TestCases.class);
        if(testCases == null)
            return;

        final Parameter[] parameters = method.getMethod().getParameters();
        for (final TestCase testCase : testCases.value()) {
            validateTestCase(errors, parameters, testCase);
        }
    }

    private void validateTestCase(final List<Throwable> errors, final Parameter[] parameters, final TestCase testCase) {
        final String[] arguments = testCase.value();
        if(arguments.length != parameters.length) {
            errors.add(new Exception("incorrect number of parameters supplied in TestCase!\r\nExpected " + parameters.length + " but there were " + arguments.length));
        } else for (int i = 0; i < parameters.length; i++) {
            validateParameterArgument(errors, parameters[i], arguments[i]);
        }
    }

    private void validateParameterArgument(final List<Throwable> errors, final Parameter parameter, final String argument) {
        try {
            CONVERTER.tryConvert(argument, parameter.getType());
        } catch (Exception e) {
            errors.add(e);
        }
    }
}
