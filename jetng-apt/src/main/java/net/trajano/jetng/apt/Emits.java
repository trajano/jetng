package net.trajano.jetng.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates the class as something that can emit code.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Emits {

    /**
     * Emitters.
     *
     * @return
     */
    Emit[]value();
}
