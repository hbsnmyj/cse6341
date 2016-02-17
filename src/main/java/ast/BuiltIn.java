package ast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BuiltIn {
    String[] operatorName();
    int expectedLength();
}
