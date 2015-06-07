package net.trajano.jetng.apt.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.StandardLocation;

import net.trajano.jetng.apt.Emit;
import net.trajano.jetng.apt.Emits;

/**
 * Processes the {@link Emit} and {@link Emits} annotations.
 */
@SupportedAnnotationTypes({ "net.trajano.jetng.apt.Emits", "net.trajano.jetng.apt.Emit" })
@SupportedSourceVersion(SourceVersion.RELEASE_5)
public class EmitsProcessor extends AbstractProcessor {

    /**
     * Known element types.
     */
    private static final Set<String> KNOWN_ELEMENT_TYPES;

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger("net.trajano.jetng.apt", "META-INF/Messages");

    static {
        KNOWN_ELEMENT_TYPES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(TypeElement.class.getName(), Element.class.getName(), VariableElement.class.getName(), ExecutableElement.class.getName(), PackageElement.class.getName())));
    }

    /**
     * Annotation processors.
     */
    private final Set<String> annotationProcessors = new TreeSet<String>();

    /**
     * Elements processed.
     */
    private final Set<TypeElement> elementCollection = new HashSet<TypeElement>();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {

        for (final Element element : roundEnv.getElementsAnnotatedWith(Emits.class)) {
            final TypeElement typeElement = (TypeElement) element;
            for (final Emit emit : typeElement.getAnnotation(Emits.class)
                    .value()) {
                processEmit(emit, typeElement);
            }
        }
        for (final Element element : roundEnv.getElementsAnnotatedWith(Emit.class)) {
            final TypeElement typeElement = (TypeElement) element;
            processEmit(typeElement.getAnnotation(Emit.class), typeElement);
        }
        if (roundEnv.processingOver() && !roundEnv.errorRaised()) {
            try {
                final PrintWriter metaWriter = new PrintWriter(processingEnv.getFiler()
                        .createResource(StandardLocation.SOURCE_OUTPUT, "", "META-INF/services/javax.annotation.processing.Processor", elementCollection.toArray(new Element[0]))
                        .openWriter());
                for (final String processor : annotationProcessors) {
                    metaWriter.println(processor);
                }
                metaWriter.close();
            } catch (final IOException e) {
                final String msg = MessageFormat.format(LOG.getResourceBundle()
                        .getString("ioerror"), "META-INF/services/javax.annotation.processing.Processor", e.getMessage());
                processingEnv.getMessager()
                .printMessage(Kind.ERROR, msg);
                throw new IllegalStateException(msg, e);
            }
        }
        return true;
    }

    /**
     * Handle the processing of an emit annotation.
     *
     * @param emit
     *            emit annotation
     * @param typeElement
     *            type element
     * @throws IOException
     */
    private void processEmit(final Emit emit,
            final TypeElement typeElement) {

        elementCollection.add(typeElement);
        String elementClassUsedInArgumentsClassConstructor = null;
        for (final Element element : typeElement.getEnclosedElements()) {
            if (ElementKind.CONSTRUCTOR == element.getKind()) {
                final ExecutableElement executableElement = (ExecutableElement) element;
                final List<? extends VariableElement> parameters = executableElement.getParameters();
                if (parameters.size() != 1 && KNOWN_ELEMENT_TYPES.contains(parameters.get(0)
                        .asType()
                        .toString())) {
                    continue;
                }
                if (elementClassUsedInArgumentsClassConstructor != null) {
                    final String msg = MessageFormat.format(LOG.getResourceBundle()
                            .getString("manyconstructor"),
                            typeElement.asType()
                            .toString());
                    processingEnv.getMessager()
                    .printMessage(Kind.ERROR, msg);
                    throw new IllegalStateException(msg);
                }
                elementClassUsedInArgumentsClassConstructor = parameters.get(0)
                        .asType()
                        .toString();
            }
        }
        if (elementClassUsedInArgumentsClassConstructor == null) {
            final String msg = MessageFormat.format(LOG.getResourceBundle()
                    .getString("noconstructor"),
                    typeElement.asType()
                    .toString());
            processingEnv.getMessager()
            .printMessage(Kind.ERROR, msg);
            throw new IllegalStateException(msg);
        }
        final EmitModel model = new EmitModel();
        if ("".equals(emit.packageName())) {
            model.setPackageName(((PackageElement) typeElement.getEnclosingElement()).getQualifiedName()
                    .toString());
        } else {
            model.setPackageName(emit.packageName());
        }
        model.setGeneratorClassName(emit.template());
        model.setClassName(emit.processorName());
        model.setArgumentClassName(typeElement.asType()
                .toString());
        model.setQualifiedNameMethod(emit.qualifiedNameMethod());
        model.setArgumentConstructorParameterType(elementClassUsedInArgumentsClassConstructor);
        model.setSupportedAnnotationTypes(emit.supportedAnnotationTypes());
        model.setSupportedSourceVersion("SourceVersion." + emit.supportedSourceVersion());
        try {
final EmitterGenerator generator = new EmitterGenerator();
            annotationProcessors.add(model.getQualifiedName());
            final PrintWriter writer = new PrintWriter(processingEnv.getFiler()
                    .createSourceFile(model.getQualifiedName())
                    .openWriter());
            generator.generate(model, writer);
            writer.close();
        } catch (final IOException e) {
            final String msg = MessageFormat.format(LOG.getResourceBundle()
                    .getString("ioerror"),
                    typeElement.asType()
                    .toString(),
                    e.getMessage());
            processingEnv.getMessager()
            .printMessage(Kind.ERROR, msg);
            throw new IllegalStateException(msg, e);
        }
    }
}
