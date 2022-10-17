package fr.lernejo.umlgrapher;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

@Command(name = "Mermaid Graph", mixinStandardHelpOptions = true, version = "java to Mermaid 1.0",
    description = "Prints the mermaid of a java class")

public class Launcher implements Callable<Integer> {

    @Option(names = {"-c", "--classes"}, description = "renseigner les classes d'où faire partir l'analyse")
    private final Class<?> [] aClass =null;

    @Option(names = {"-g", "--graph-type"}, description = "sélectionner le type de graph que l'on souhaite en sortie")
    private final UmlGraph.GraphType graphType = UmlGraph.GraphType.Mermaid ;
    @Override
    public Integer call() throws Exception { // your business logic goes here...

        UmlGraph graph = new UmlGraph(aClass[0]);

        String output = graph.as(UmlGraph.GraphType.Mermaid);
        System.out.println(output);
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
