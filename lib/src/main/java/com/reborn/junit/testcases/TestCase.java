package com.reborn.junit.testcases;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(TestCases.class)
@Target({ElementType.METHOD})
public @interface TestCase {
    String[] value() default {};
}
