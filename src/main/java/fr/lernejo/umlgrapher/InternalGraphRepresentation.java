package fr.lernejo.umlgrapher;

import org.apache.maven.surefire.shade.org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class InternalGraphRepresentation {

    private final Class parentClass ;
    private final Class[] interfaces ;
    private final String name;
    private final  String packageName;
    private final String type;
    private final UmlRelation relation ;
    public InternalGraphRepresentation(Class aClass, String type){
        this.name = aClass.getSimpleName();
        this.packageName = aClass.getPackageName();
        this.parentClass = aClass.getSuperclass();
        this.interfaces = aClass.getInterfaces();
        this.type = type ;
        this.relation = new UmlRelation(this);
    }

    public String name() {
        return this.name;
    }

    public String packageName() {
        return this.packageName;
    }

    public String getType(){
        return this.type;
    }

    public Class[] getAllInterfaces(){
        Class[] interfaces=null;
        if (this.getInterfaces() == null || this.getInterfaces().length==0 )
            return this.getInterfaces();
        else {
            for (int i=0; i<this.getInterfaces().length;i++)
                interfaces =  ArrayUtils.addAll(this.getInterfaces(), new InternalGraphRepresentation(this.getInterfaces()[i],new UmlType(this.getInterfaces()[0]).type()).getAllInterfaces());
            return interfaces;
        }
    }

    public Class[] getInterfaces()
    {
        return this.interfaces;
    }

    public Class getSuperClass()
    {
        return this.parentClass;
    }

    public UmlRelation getRelation() {
        return relation;
    }
}
