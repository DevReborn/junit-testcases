package com.reborn.junit.testcases;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCaseMethodFilter extends Filter {
    private static final Pattern TEST_CASE_DESCRIPTION_PATTERN = Pattern.compile("^(\\w*?)\\(.*?\\)\\(([\\w.$]*?)\\)$", Pattern.DOTALL);

    private final Filter _filter;

    public TestCaseMethodFilter(final Filter filter) {
        _filter = filter;
    }

    @Override
    public boolean shouldRun(final Description description) {
        final Matcher matcher = TEST_CASE_DESCRIPTION_PATTERN.matcher(description.toString());
        if(!matcher.matches())
            return _filter.shouldRun(description);

        final boolean result = _filter.shouldRun(Description.createTestDescription(
                description.getTestClass(),
                matcher.group(1),
                description.getAnnotations().toArray(new Annotation[0])));

        if (result) {
            return true;
        }

        for (final Description child : description.getChildren()) {
            if (shouldRun(child)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String describe() {
        return _filter.describe();
    }
}
