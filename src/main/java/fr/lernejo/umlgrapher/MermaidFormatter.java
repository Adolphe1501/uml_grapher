package fr.lernejo.umlgrapher;


public class MermaidFormatter {

    public String format(InternalGraphRepresentation[] graph)
    {
        StringBuilder format = new StringBuilder("classDiagram\n");
        for (InternalGraphRepresentation graphRepresentation : graph) {
            if (graphRepresentation.getType().equals("<<interface>>"))
                format.append("""
                    class %s {
                        %s
                    }
                    """.formatted(graphRepresentation.name(), graphRepresentation.getType()));
            else
                format.append("""
                    class %s
                    """.formatted(graphRepresentation.name()));
        }
        format.append(this.formatRelation(graph));
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
                        """.formatted(graphRepresentation.getRelation().getInterfaceParente()[j], graphRepresentation.name()));
                }
            } else {
                format.append(formatClassRelation(graphRepresentation));
            }
        }
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
        {
            format.append("""
                        %s <|-- %s : extends
                        """.formatted(graph.getSuperClass().getSimpleName(), graph.name()));
        }
        return  format.toString();
    }
}
