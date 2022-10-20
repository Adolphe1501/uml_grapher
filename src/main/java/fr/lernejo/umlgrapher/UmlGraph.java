package fr.lernejo.umlgrapher;
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
        for (int i=0; i< this.umlClass.length; i++) {
            umlType = new UmlType(this.umlClass[i]);
            graphRepresentation = new InternalGraphRepresentation(this.umlClass[i], umlType.type());
            this.graph.add(graphRepresentation);
            Class [] interfaces = graphRepresentation.getAllInterfaces() ;
            buildInternalInterface(interfaces);
        }
    }
    private void buildInternalInterface(Class[] interfaces)
    {
        InternalGraphRepresentation graphRepresentationInterface;
        UmlType umlTypeInterface = null ;
        if(interfaces != null)
        {
            for(int j=0; j< interfaces.length; j++)
            {
                umlTypeInterface = new UmlType(interfaces[j]);
                graphRepresentationInterface = new InternalGraphRepresentation(interfaces[j], umlTypeInterface.type());
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
