package me.dong.simple_android;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.inject.Qualifier;

@Qualifier
@Retention(RUNTIME)
public @interface ForApplication {
}
