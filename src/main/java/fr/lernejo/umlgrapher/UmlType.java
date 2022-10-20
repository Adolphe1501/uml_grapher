package fr.lernejo.umlgrapher;

import java.lang.reflect.Modifier;

public class UmlType {

    private final String type ;

    public  UmlType(Class aClass){
        String type1="";
        if(Modifier.isInterface(aClass.getModifiers())){
            type1 = "<<interface>>";
        }

        this.type = type1;
    }

    public String type() {
        return this.type;
    }
}
