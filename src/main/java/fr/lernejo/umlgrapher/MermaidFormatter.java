package fr.lernejo.umlgrapher;
import java.lang.reflect.*;
public class MermaidFormatter {
    public String format(InternalGraphRepresentation[] graph) {
        StringBuilder format = new StringBuilder("classDiagram\n");
        for (InternalGraphRepresentation graphRepresentation : graph) {
            if (graphRepresentation.getType().equals("<<interface>>")) {
                String methods = this.formatMethods(graphRepresentation);
                if ( methods == "" || methods==null || methods.length()<1)  methods="\n";
                format.append("""
                    class %s {
                        %s%s}
                    """.formatted(graphRepresentation.name(), graphRepresentation.getType(),methods));
            }else{
                String classFormat = formatFields(graphRepresentation) + formatMethods(graphRepresentation);
                if(classFormat.length()>0) classFormat = " {" + formatFields(graphRepresentation) + formatMethods(graphRepresentation) +"}";
                format.append("""
                    class %s%s
                    """.formatted(graphRepresentation.name(),classFormat));}}
        format.append(this.formatRelation(graph));
        format.append(this.formatRelationWithClasses(graph));
        return format.toString();
    }
    private String formatFields(InternalGraphRepresentation graph){
        StringBuilder format = new StringBuilder();
        String access = "-";
        String caracteristic = "$";
        for (Field field : graph.getFields()){
            if(!Modifier.isPrivate(field.getModifiers())) access ="+";
            if (!Modifier.isStatic(field.getModifiers())) caracteristic ="";
            format.append("""
                    %s%s %s%s
                """.formatted(access,field.getType().getSimpleName(), field.getName(),caracteristic));}
        if(format.length()>0) format.insert(0,"\n");
        return format.toString();
    }
    private String formatMethods( InternalGraphRepresentation graph){
        StringBuilder format = new StringBuilder();
        String access = "";
        String caracteristic = "";
        for (Method method : graph.getMethods()){
            if(method.getName()!="$jacocoInit"){
                if(!Modifier.isPrivate(method.getModifiers())) access ="+";
                else access ="-";
                if (!Modifier.isStatic(method.getModifiers())) caracteristic ="";
                else caracteristic ="$";
                Parameter[] parameterss = method.getParameters();
                String parameters ="";
                if(parameterss.length>0)
                    for (Parameter parameter : parameterss) parameters =  parameter.getType().getSimpleName() + " " + parameter.getName() ;
                format.append("""
                        %s%s(%s)%s %s
                    """.formatted(access,method.getName(),parameters,caracteristic,method.getReturnType().getSimpleName()));}}
        if(formatFields(graph).length()==0 && format.length()>0) format.insert(0,"\n");
        return format.toString();
    }
    private String formatRelation(InternalGraphRepresentation [] graph)
    {
        StringBuilder format = new StringBuilder();
        for (InternalGraphRepresentation graphRepresentation : graph) {
            if (graphRepresentation.getType().equals("<<interface>>")) {
                for (int j = 0; j < graphRepresentation.getRelation().getInterfaceParente().length; j++) {
                    format.append("""
                        %s <|-- %s : extends
                        """.formatted(graphRepresentation.getRelation().getInterfaceParente()[j], graphRepresentation.name()));}
            } else format.append(formatClassRelation(graphRepresentation));}
        return format.toString();
    }
    private String formatClassRelation(InternalGraphRepresentation graph){
        StringBuilder format = new StringBuilder();

        for (int j=0; j<graph.getRelation().getInterfaceParente().length;j++) {
            format.append("""
                        %s <|.. %s : implements
                        """.formatted(graph.getRelation().getInterfaceParente()[j], graph.name()));
        }
        if (graph.getSuperClass() !=null && !graph.getSuperClass().getSimpleName().equals("Object"))
            format.append("""
                        %s <|-- %s : extends
                        """.formatted(graph.getSuperClass().getSimpleName(), graph.name()));
        return  format.toString();
    }
    private String formatRelationWithClasses(InternalGraphRepresentation[] graphRepresentations)
    {
        StringBuilder format = new StringBuilder();
        for (InternalGraphRepresentation graphRepresentation : graphRepresentations){
            for (Field field : graphRepresentation.getFields()) {
                for (InternalGraphRepresentation graph : graphRepresentations) {
                    if (field.getType().getSimpleName().equals(graph.name())) {
                        if (!field.getType().getSimpleName().equals(graphRepresentation.name())) {
                            format.append("""
                                %s <-- %s : uses
                                """.formatted(graph.name(), graphRepresentation.name()));
                        } else {
                            format.append("""
                                %s <-- %s : returns
                                """.formatted(graph.name(), graphRepresentation.name()));}}}}}
        return format.toString();
    }
}
