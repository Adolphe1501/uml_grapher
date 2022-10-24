package fr.lernejo.umlgrapher;

public class UmlRelation {

    private final String [] interfaceParente;
    public UmlRelation(InternalGraphRepresentation graphRepresentation){
        this.interfaceParente = this.getInterfaceParentes(graphRepresentation);
    }

    public String[] getInterfaceParentes(InternalGraphRepresentation graphRepresentation){
        String[] output = new String[graphRepresentation.getInterfaces().length];
        if(graphRepresentation.getInterfaces() != null && graphRepresentation.getInterfaces().length >0){
            Class [] result = graphRepresentation.getInterfaces();
            for (int i=0; i< graphRepresentation.getInterfaces().length; i++)
                output[i] = result[i].getSimpleName();
        }
        return output;
    }

    public String[] getInterfaceParente() {
        return interfaceParente;
    }
}
