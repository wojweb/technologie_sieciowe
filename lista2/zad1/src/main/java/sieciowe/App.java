package sieciowe;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.*;

import java.util.Random;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String path = "/home/pan/Studia/Semestr4/sieci/lista2/zad1/data/";
        int numberOfAttempts = 100000;

        String fileName = "graphA.json";
        MyGraph myGraph = new MyGraph(path + fileName);
        System.out.println("Prawdopodobienstwo spojnosci w a: " + myGraph.makeSimulation(numberOfAttempts));

        myGraph.addEdge(20, 1, 0.95);
        System.out.println("Prawdopodobienstwo spojnosci w b: " + myGraph.makeSimulation(numberOfAttempts));

        myGraph.addEdge(1, 10, 0.8);
        myGraph.addEdge(5, 15 , 0.7);
        System.out.println("Prawdopodobienstwo spojnosci w c: " + myGraph.makeSimulation(numberOfAttempts));



        Random random = new Random();
        double reliability = 0.4;
        int [] vertices = new int[8];
        for(int j = 0; j < 8; j++)
            vertices[j] = random.nextInt(20);

        myGraph.addEdge(vertices[0], vertices[1], reliability);
        myGraph.addEdge(vertices[2], vertices[3], reliability);
        myGraph.addEdge(vertices[4], vertices[5], reliability);
        myGraph.addEdge(vertices[6], vertices[7], reliability);

        System.out.println("Prawdopodobienstwo spojnosci w d: " + myGraph.makeSimulation(numberOfAttempts));






    }





    private Graph loadGraph(){



        return null;
    }


}
