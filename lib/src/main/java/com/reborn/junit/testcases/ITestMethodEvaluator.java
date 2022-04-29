package com.reborn.junit.testcases;

import org.junit.runners.model.FrameworkMethod;

import java.util.List;

public interface ITestMethodEvaluator {
    List<FrameworkMethod> evaluateTestMethods(final List<FrameworkMethod> testMethods);
}
