package com.hanframework.mojito.processor;


import java.lang.annotation.*;

/**
 * 数值越大优先级越大
 *
 * @author liuxin
 * 2020-09-01 23:00
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MojitoOrder {
    int value() default 0;
}
