package blah;

@net.trajano.jetng.apt.Emit(processorName = "SampleProcessor2", template = "foo.Bar", supportedAnnotationTypes = "javax.persistence.Entity")
public class Sample2 {

    public Sample2(javax.lang.model.element.TypeElement element) {
    }
    
    public String getQualifiedName() {
        return "generated.Nyaa";
    }
}
