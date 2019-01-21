package com.viperhcf.minssentials.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleCommand {
    boolean requireop();

    String name();

    String[] allias() default {};
}
