package fr.lernejo.umlgrapher;

import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

public class UmlGraph {

    private final Class umlClass;

    public UmlGraph(Class umlClass){
        this.umlClass = umlClass ;

    }
    public final String as( GraphType gt)
    {
        return gt.func.apply(this.umlClass);
    }


    public enum GraphType{

        Mermaid((aClass) -> "classDiagram\n" +
                               "class "+ aClass.getSimpleName() +" {\n" +
                               "    <<interface>>\n" +
                               "}\n"),
        ;
        private final Function<Class,String> func;

        GraphType(Function<Class,String> func) {
            this.func = func;
        }
    }




}
