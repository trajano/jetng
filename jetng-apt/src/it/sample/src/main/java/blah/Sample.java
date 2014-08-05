package blah;

@net.trajano.jetng.apt.Emits(@net.trajano.jetng.apt.Emit(processorName = "SampleProcessor", template = "foo.Bar", supportedAnnotationTypes = "javax.persistence.Entity"))
public class Sample {

    public Sample(javax.lang.model.element.TypeElement element) {
    }
    
    public String getQualifiedName() {
        return "generated.Nyaa";
    }
}
