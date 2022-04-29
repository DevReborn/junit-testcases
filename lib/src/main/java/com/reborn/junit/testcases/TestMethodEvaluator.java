package com.reborn.junit.testcases;

import org.junit.runners.model.FrameworkMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestMethodEvaluator implements ITestMethodEvaluator {
    @Override
    public List<FrameworkMethod> evaluateTestMethods(final List<FrameworkMethod> testMethods) {
        final ArrayList<FrameworkMethod> frameworkMethods = new ArrayList<>();
        for (final FrameworkMethod method : testMethods) {
            final TestCases testCases = method.getAnnotation(TestCases.class);
            final TestCase testCase = method.getAnnotation(TestCase.class);
            if(testCases == null && testCase == null) {
                frameworkMethods.add(method);
            } else {
                final TestCase[] cases = testCases == null ? new TestCase[] { testCase } : testCases.value();
                final List<FrameworkTestCaseMethod> testCaseMethods = Arrays.stream(cases)
                        .map(x -> new FrameworkTestCaseMethod(method.getMethod(), x))
                        .collect(Collectors.toList());
                frameworkMethods.addAll(testCaseMethods);
            }
        }
        return frameworkMethods;
    }
}
