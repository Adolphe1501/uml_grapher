package fr.lernejo.umlgrapher;

import org.apache.maven.surefire.shade.org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Set;
import java.util.concurrent.Callable;

@Command(name = "Mermaid Graph", mixinStandardHelpOptions = true, version = "java to Mermaid 1.0",
    description = "Prints the mermaid of java class")

public class Launcher implements Callable<Integer> {

    @Option(names = {"-c", "--classes"}, description = "renseigner les classes d'où faire partir l'analyse")
    private final Class<?> [] aClass ={};

    @Option(names = {"-g", "--graph-type"}, description = "sélectionner le type de graph que l'on souhaite en sortie")
    private final GraphType graphType = GraphType.Mermaid ;
    @Override
    public Integer call() throws Exception { // your business logic goes here...

        UmlGraph umlGraph = new UmlGraph(aClass);
        System.out.println(umlGraph.as(graphType));
        return 0;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new Launcher()).execute(args);
        System.exit(exitCode);

       /* UmlRelation relation = new UmlRelation();
        Class obj = relation.getClass();
        relation.setUmlRelation(obj.getSuperclass(),obj.getInterfaces(), Modifier.isInterface(obj.getModifiers()));
    */
    }
}
