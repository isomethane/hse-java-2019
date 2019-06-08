package ru.hse.mnmalysheva.myjunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation for test methods. **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    /** Expected exception. **/
    Class<? extends Throwable> expected() default NoException.class;
    /** If test should be ignored, ignore parameter should be non-empty string. **/
    String ignore() default "";
}
