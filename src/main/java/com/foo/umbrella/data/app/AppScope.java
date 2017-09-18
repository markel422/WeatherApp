package com.foo.umbrella.data.app;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by mike0 on 9/18/2017.
 */

@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScope {
}
