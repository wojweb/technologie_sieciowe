package sieciowe;

import java.util.Iterator;

public class Test {
    public static void main(String[] args) {
        String path = "/home/pan/Studia/Semestr4/sieci/lista2/zad1/data/";
        String fileName = "graphA.json";

        MyGraph graph = new MyGraph(path + fileName);
//        System.out.println(graph.getMatrix().get(5).get(5));
        graph.fillA(graph.getGraph());
        graph.fillC(10);
        System.out.println(graph.getEdges().get(2).getA());
        Iterator it = graph.getEdges().iterator();

        System.out.println(graph.averageDelay(2));

        System.out.println("Teraz bedzie symulacja");
        System.out.println(graph.makeSimulation2(1000, 0.0035, 512));
    }
}
