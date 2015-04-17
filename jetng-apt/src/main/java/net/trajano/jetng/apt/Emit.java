package net.trajano.jetng.apt;

import javax.lang.model.SourceVersion;

/**
 * Code emitter. Annotated classes must have one constructor that takes in an
 * element.
 */
public @interface Emit {

    /**
     * Package name where the generator class will reside, if it is not
     * specified, then the package name of the class annotated will be used.
     *
     * @return
     */
    String packageName() default "";

    /**
     * Name of the processor class that will be generated.
     *
     * @return
     */
    String processorName();

    /**
     * Method name found in the annotated class used to get the qualified name
     * of the written class.
     *
     * @return
     */
    String qualifiedNameMethod() default "getQualifiedName";

    /**
     * Supported annotation types. This is a String[] as APT cannot access the
     * class itself.
     *
     * @return
     */
    String[]supportedAnnotationTypes();

    /**
     * Supported source version.
     *
     * @return
     */
    SourceVersion supportedSourceVersion() default SourceVersion.RELEASE_6;

    /**
     * JET template class. This is a String as APT cannot access the class
     * itself.
     *
     * @return
     */
    String template();
}
