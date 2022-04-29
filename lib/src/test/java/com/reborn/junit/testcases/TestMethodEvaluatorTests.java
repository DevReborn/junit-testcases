package com.reborn.junit.testcases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import org.junit.runners.model.FrameworkMethod;

import java.util.ArrayList;
import java.util.List;

class TestMethodEvaluatorTests {

    @Test
    void testWith_NoTestCases() throws Exception {
        // Given
        final TestMethodEvaluator evaluator = new TestMethodEvaluator();
        final ArrayList<FrameworkMethod> methods = new ArrayList<>();
        methods.add(new FrameworkMethod(FakeTests.class.getMethod("noTestCases")));

        // When
        final List<FrameworkMethod> result = evaluator.evaluateTestMethods(methods);

        // Then
        assertEquals(1, result.size());
        assertEquals(methods.get(0), result.get(0));
    }

    @Test
    void testWith_SingleTestCase() throws Exception {
        // Given
        final TestMethodEvaluator evaluator = new TestMethodEvaluator();
        final ArrayList<FrameworkMethod> methods = new ArrayList<>();
        methods.add(new FrameworkMethod(FakeTests.class.getMethod("singleTestCase", int.class)));

        // When
        final List<FrameworkMethod> result = evaluator.evaluateTestMethods(methods);

        // Then
        assertEquals(1, result.size());
        assertInstanceOf(FrameworkTestCaseMethod.class, result.get(0));
        final FrameworkTestCaseMethod method = (FrameworkTestCaseMethod) result.get(0);
        assertEquals(1, method.getParameters().length);
        assertEquals(10, method.getParameters()[0]);
        assertEquals("singleTestCase(10)", method.getName());
    }

    @Test
    void testWith_MultipleTestCases() throws Exception {
        // Given
        final TestMethodEvaluator evaluator = new TestMethodEvaluator();
        final ArrayList<FrameworkMethod> methods = new ArrayList<>();
        methods.add(new FrameworkMethod(FakeTests.class.getMethod("multipleTestCases", int.class)));

        // When
        final List<FrameworkMethod> result = evaluator.evaluateTestMethods(methods);

        // Then
        assertEquals(2, result.size());
        assertInstanceOf(FrameworkTestCaseMethod.class, result.get(0));
        assertInstanceOf(FrameworkTestCaseMethod.class, result.get(1));
        final FrameworkTestCaseMethod method1 = (FrameworkTestCaseMethod) result.get(0);
        final FrameworkTestCaseMethod method2 = (FrameworkTestCaseMethod) result.get(1);
        assertEquals(10, method1.getParameters()[0]);
        assertEquals(20, method2.getParameters()[0]);
    }
}

class FakeTests {
    @org.junit.Test
    public void noTestCases() {

    }

    @org.junit.Test
    @TestCase({"10"})
    public void singleTestCase(final int arg) {

    }

    @org.junit.Test
    @TestCase({"10"})
    @TestCase({"20"})
    public void multipleTestCases(final int arg) {

    }
}