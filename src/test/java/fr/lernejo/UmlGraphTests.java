package fr.lernejo;

import fr.lernejo.umlgrapher.GraphType;
import fr.lernejo.umlgrapher.UmlGraph;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

 class UmlGraphTests {
    @Test
    void empty_interface_with_no_relation() {
        UmlGraph graph = new UmlGraph(new Class[]{Machin.class});

        String output = graph.as(GraphType.Mermaid);

        Assertions.assertThat(output).isEqualTo("""
            classDiagram
            class Machin {
                <<interface>>
            }
            """);
    }

    @Test

    void empty_interface_with_relation() {
        UmlGraph graph = new UmlGraph(new Class[]{Living.Animal.Ant.class, Living.Animal.Cat.class,Living.Plant.Tree.Alder.class});

        String output = graph.as(GraphType.Mermaid);

        Assertions.assertThat(output).isEqualTo("""
           classDiagram
           class Alder
           class Animal {
               <<interface>>
           }
           class Ant
           class Cat
           class Living {
               <<interface>>
           }
           class Plant {
               <<interface>>
           }
           class Tree {
               <<interface>>
           }
           Tree <|.. Alder : implements
           Living <|-- Animal : extends
           Animal <|.. Ant : implements
           Animal <|.. Cat : implements
           Living <|-- Plant : extends
           Plant <|-- Tree : extends
           """);
    }

    @Test
    void field_Single_ClassWithRelation(){
        UmlGraph graph = new UmlGraph(new Class[]{Singleton.class});

        String output = graph.as(GraphType.Mermaid);

        Assertions.assertThat(output).isEqualTo("""
            classDiagram
            class Singleton {
                -Singleton instance$
                +getInstance()$ Singleton
                +supplySomeStr(int offset) String
            }
            Singleton <-- Singleton : returns
            """);
    }

    @Test
    void field_ClassWith_Child_and_Relation(){
        UmlGraph graph = new UmlGraph(new Class[]{Image.class});

        String output = graph.as(GraphType.Mermaid);

        Assertions.assertThat(output).isEqualTo("""
            classDiagram
            class Image {
                <<interface>>
                +display() void
            }
            class LazyLoadedImage {
                -String fileName
                -RealImage realImage
                +display() void
            }
            class RealImage {
                -String fileName
                +display() void
                -loadFromDisk(String fileName) void
            }
            Image <|.. LazyLoadedImage : implements
            Image <|.. RealImage : implements
            RealImage <-- LazyLoadedImage : uses
            """);
    }

    interface Machin {
    }
    public sealed interface Living {
        sealed interface Animal extends Living {
            final class Ant implements Animal {
            }

            final class Cat implements Animal {
            }
        }

        sealed interface Plant extends Living {
            sealed interface Tree extends Plant {
                final class Alder implements Tree {
                }
            }
        }
    }

    public static class Singleton {

        private static final Singleton instance = new Singleton();

        public static Singleton getInstance() {
            return instance;
        }

        public String supplySomeStr(int offset) {
            return String.valueOf(43 + offset);
        }
    }

    public sealed interface Image {

        void display();

        final class RealImage implements Image {

            private final String fileName;

            public RealImage(String fileName){
                this.fileName = fileName;
                loadFromDisk(fileName);
            }

            @Override
            public void display() {
                System.out.println("Displaying " + fileName);
            }

            private void loadFromDisk(String fileName){
                System.out.println("Loading " + fileName);
            }
        }

        final class LazyLoadedImage implements Image{

            private final String fileName;

            private RealImage realImage;

            public LazyLoadedImage(String fileName){
                this.fileName = fileName;
            }

            @Override
            public void display() {
                if(realImage == null){
                    realImage = new RealImage(fileName);
                }
                realImage.display();
            }
        }
    }
}
