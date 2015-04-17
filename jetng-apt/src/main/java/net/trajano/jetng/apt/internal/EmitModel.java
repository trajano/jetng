package net.trajano.jetng.apt.internal;

/**
 * Emit model passed into the code generator.
 */
public class EmitModel {

    /**
     * Argument class name.
     */
    private String argumentClassName;

    /**
     * Constructor parameter type for the argument class.
     */
    private String argumentConstructorParameterType;

    /**
     * Class name for the generated processor class.
     */
    private String className;

    /**
     * JET code generator class name.
     */
    private String generatorClassName;

    /**
     * Package name.
     */
    private String packageName;

    /**
     * Method name used to look up the qualified name of the resulting generated
     * code.
     */
    private String qualifiedNameMethod;

    /**
     * Supported annotation types.
     */
    private String[] supportedAnnotationTypes;

    /**
     * Supported source version.
     */
    private String supportedSourceVersion;

    public String getArgumentClassName() {

        return argumentClassName;
    }

    public String getArgumentConstructorParameterType() {

        return argumentConstructorParameterType;
    }

    public String getClassName() {

        return className;
    }

    public String getGeneratorClassName() {

        return generatorClassName;
    }

    public String getPackageName() {

        return packageName;
    }

    public String getQualifiedName() {

        return packageName + "." + className;
    }

    public String getQualifiedNameMethod() {

        return qualifiedNameMethod;
    }

    public String[] getSupportedAnnotationTypes() {

        return supportedAnnotationTypes;
    }

    /**
     * Gets the supported annotation types as as string suitable for the
     * {@link javax.annotation.processing.SupportedAnnotationTypes} annotation.
     *
     * @return supported annotation types string.
     */
    public String getSupportedAnnotationTypesString() {

        if (supportedAnnotationTypes.length == 1) {
            return "\"" + supportedAnnotationTypes[0] + "\"";
        }
        final StringBuilder b = new StringBuilder("{");
        for (final String supportedAnnotationType : supportedAnnotationTypes) {
            b.append('"');
            b.append(supportedAnnotationType);
            b.append('"');
            b.append(',');
        }
        b.setLength(b.length() - 1);
        b.append('}');
        return b.toString();

    }

    public String getSupportedSourceVersion() {

        return supportedSourceVersion;
    }

    public void setArgumentClassName(final String argumentClassName) {

        this.argumentClassName = argumentClassName;
    }

    public void setArgumentConstructorParameterType(final String argumentConstructorParameterType) {

        this.argumentConstructorParameterType = argumentConstructorParameterType;
    }

    public void setClassName(final String className) {

        this.className = className;
    }

    public void setGeneratorClassName(final String generatorClassName) {

        this.generatorClassName = generatorClassName;
    }

    public void setPackageName(final String packageName) {

        this.packageName = packageName;
    }

    public void setQualifiedNameMethod(final String qualifiedNameMethod) {

        this.qualifiedNameMethod = qualifiedNameMethod;
    }

    public void setSupportedAnnotationTypes(final String[] supportedAnnotationTypes) {

        this.supportedAnnotationTypes = supportedAnnotationTypes;
    }

    public void setSupportedSourceVersion(final String supportedSourceVersion) {

        this.supportedSourceVersion = supportedSourceVersion;
    }
}
