package com.lccf.util.excel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface ExcelValidAttribute {
    boolean required() default false;
    String regexp() default "";
    String message() default "";
    int min() default 0;
    int max()  default Integer.MAX_VALUE;
}
