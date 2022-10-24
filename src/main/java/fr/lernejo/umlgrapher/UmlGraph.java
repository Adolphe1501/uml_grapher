package fr.lernejo.umlgrapher;
import org.apache.maven.surefire.shade.org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
public class UmlGraph {
    private final Class[] umlClass;
    private final Set<InternalGraphRepresentation> graph = new TreeSet<>(Comparator.<InternalGraphRepresentation, String>comparing(t->t.name()).thenComparing(t->t.packageName()));
    public UmlGraph(Class []umlClass){
        this.umlClass = umlClass ;
    }
    public void buildGraph(GraphType graphType ){
        InternalGraphRepresentation graphRepresentation;
        UmlRelation umlRelation = null ;
        UmlType umlType = null ;
        Class[] classes = null;
        for (int i=0; i< this.umlClass.length; i++) {
            umlType = new UmlType(this.umlClass[i]);
            graphRepresentation = new InternalGraphRepresentation(this.umlClass[i], umlType.type());
            this.graph.add(graphRepresentation);
            Class [] interfaces = graphRepresentation.getAllInterfaces() ;
            buildInternalClass(interfaces);
            classes = ArrayUtils.addAll(classes,this.umlClass[i]);
            classes = ArrayUtils.addAll(classes,interfaces);
        }

        buildChildGraph(classes);

    }
    public void buildChildGraph(Class [] classes)
    {
        if (classes !=null && classes.length>0) {
            Class [] newParentsClasses = null;
            for (int i = 0; i < classes.length; i++) {
                Class[] childClasses = getAllChildClass(classes[i]);
                for (int j = 0; j < childClasses.length; j++) {
                    boolean present = false;
                    for (int k = 0; k < classes.length; k++)
                        if (childClasses[j] == classes[k]) present = true;
                    if (present==false) {
                        UmlType umlType = new UmlType(childClasses[j]);
                        InternalGraphRepresentation graphRepresentation = new InternalGraphRepresentation(childClasses[j], umlType.type());
                        this.graph.add(graphRepresentation);
                        newParentsClasses = ArrayUtils.addAll(newParentsClasses,childClasses[j]);
                    }
                }
            } buildChildGraph(newParentsClasses);
        }
    }
    public Class[] getAllChildClass(Class umlClass){
//        Class[] theClass = null ;
/*
        for (int i = 0; i < umlClass.length; i++) {
            reflections = new Reflections(new ConfigurationBuilder()
                .forPackage("")
                .forPackage("", umlClass.getClassLoader())
            );
*/
        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .forPackage("")
            .forPackage("", umlClass.getClassLoader()) );
        Set<Class<?>> subTypes = reflections.get(
            Scanners.SubTypes
                .get(umlClass)
                .asClass(this.getClass().getClassLoader(), umlClass.getClassLoader())
        ); // will contain [Ant.class, Cat.class]
        return subTypes.toArray(new Class[subTypes.size()]);
    }
    private void buildInternalClass(Class[] classes)
    {
        InternalGraphRepresentation graphRepresentationInterface;
        UmlType umlTypeInterface = null ;
        if(classes != null)
        {
            for(int j=0; j< classes.length; j++)
            {
                umlTypeInterface = new UmlType(classes[j]);
                graphRepresentationInterface = new InternalGraphRepresentation(classes[j], umlTypeInterface.type());
                this.graph.add(graphRepresentationInterface);
            }
        }
    }
    public Set<InternalGraphRepresentation> getGraph(){
        return this.graph;
    }
    public final String as( GraphType graphType)
    {
        if (graphType.equals(GraphType.Mermaid)) {
            this.buildGraph(GraphType.Mermaid);
            return new MermaidFormatter().format(this.graph.toArray(new InternalGraphRepresentation[this.graph.size()]));
        }else
            return "Only Mermaid Type can be apply actually";
    }
}
