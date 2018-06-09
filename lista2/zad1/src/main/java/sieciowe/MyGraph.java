package sieciowe;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;


import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.util.*;



public class MyGraph {

    private transient String path;
    private List<String> vertices = new LinkedList<>();
    private List<MyEdge> edges = new LinkedList<>();
    private List<List<Integer>> matrix = new LinkedList<>();

    double makeSimulation(int numberOfAttempts){
        int counter = 0;

        for(int i = 0;  i < numberOfAttempts; i++){
            Graph<String, DefaultEdge> graph = getGraph();
            ConnectivityInspector<String, DefaultEdge> inspector = new ConnectivityInspector<>(graph);
            if(inspector.isGraphConnected())
                counter++;

        }

        double probability = (double) counter / numberOfAttempts;
        return probability;
    }

    double makeSimulation2(int numberOfAttemps, double Tmax, double sredniaWielkoscPakietu){
        int counter = 0;
        Graph<String, DefaultEdge> graph  = getGraphWithoutProbability();
        fillA(graph);
        fillC(5);
        double Tsr = averageDelay(sredniaWielkoscPakietu);
        if(Tsr > Tmax)
            return 0;
        for(int i = 0; i < numberOfAttemps; i++){
            graph = getGraph();
            ConnectivityInspector<String, DefaultEdge> inspector = new ConnectivityInspector<>(graph);
            if(!inspector.isGraphConnected())
                continue;

            fillA(graph);
            int j ;
            for(j = 0; j < edges.size(); j++)
                if(edges.get(j).getC()< edges.get(j).getA() * sredniaWielkoscPakietu)
                    break;
            if(j != edges.size())
                continue;
            if(averageDelay(sredniaWielkoscPakietu) > Tmax)
                continue;
            counter++;
        }

        double probability = (double) counter / numberOfAttemps;
        return probability;

    }
    boolean addEdge(int vertexNumber1, int vertexNumber2,double reliability){
        try{
            String vertex1 = vertices.get(vertexNumber1 - 1);
            String vertex2 = vertices.get(vertexNumber2 - 1);

            edges.add(new MyEdge(vertex1, vertex2, reliability));
            return true;
        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }
    boolean deleteEdge(int vertexNumber1, int vertexNumber2, double reliability){
        try{
            String vertex1 = vertices.get(vertexNumber1 - 1);
            String vertex2 = vertices.get(vertexNumber2 - 1);

            return edges.remove(new MyEdge(vertex1, vertex2, reliability));
        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    Graph<String, DefaultEdge> getGraph(){
        Graph<String, DefaultEdge> graph = new Pseudograph<String, DefaultEdge>(DefaultEdge.class);
        Iterator it = vertices.iterator();
        while (it.hasNext())
            graph.addVertex((String) it.next());

        Random rand = new Random();
        it = edges.iterator();
        while(it.hasNext()){
            MyEdge edge =(MyEdge) it.next();
            float number = rand.nextFloat();
            if(number > edge.reliability)
                continue;

            graph.addEdge(edge.vertex1, edge.vertex2);
        }

        return graph;
    }

    Graph<String, DefaultEdge> getGraphWithoutProbability(){
        Graph<String, DefaultEdge> graph = new Pseudograph<String, DefaultEdge>(DefaultEdge.class);
        Iterator it = vertices.iterator();
        while (it.hasNext())
            graph.addVertex((String) it.next());

        it = edges.iterator();
        while(it.hasNext()){
            MyEdge edge =(MyEdge) it.next();
            graph.addEdge(edge.vertex1, edge.vertex2);
        }

        return graph;
    }

    void fillA(Graph<String, DefaultEdge> graph){
        Iterator it =edges.iterator();
        while (it.hasNext())
            ((MyEdge)it.next()).setA(0);


        for(int i = 0; i < vertices.size(); i++){
            String v1 = vertices.get(i);
            for(int j = 0; j < vertices.size(); j++){
                String v2 = vertices.get(j);
                GraphPath<String, DefaultEdge> pathBetween = DijkstraShortestPath.findPathBetween(graph, v1, v2);
                if(pathBetween == null)
                    continue;
//                System.out.println("i = " + i + " j = " + j + pathBetween);
                List<DefaultEdge> list = pathBetween.getEdgeList();
//                System.out.println(list);
                Iterator edgesIterator = list.iterator();
                while(edgesIterator.hasNext()){
                    DefaultEdge edge = (DefaultEdge) edgesIterator.next();
//                    System.out.println("from: " + graph.getEdgeSource(edge) + " to: " + graph.getEdgeTarget(edge));
                    MyEdge myEdge = edges.get(edges.indexOf(new MyEdge(graph.getEdgeTarget(edge), graph.getEdgeSource(edge))));
                    int matrix_value;
                    if(i >= matrix.size() || j >= matrix.get(i).size())
                        matrix_value = 0;
                    else
                        matrix_value = matrix.get(i).get(j);
//                    System.out.println("Krawedz: " + v1 + " " + v2 + " value: " + matrix_value);
                    myEdge.setA(myEdge.getA() + matrix_value);
//
                }

            }
        }

    }

    public void fillC(double mult){
        Iterator it = edges.iterator();
        while (it.hasNext()){
            MyEdge edge = (MyEdge) it.next();
            edge.setC(edge.getA() * mult + 1);
        }
    }

    public double averageDelay(double m){
        double g = 0;
        double sum = 0;
        for(int i = 0; i < matrix.size(); i++){
            for(int j = 0; j < matrix.get(i).size(); j++)
                g+=matrix.get(i).get(j);
        }
        Iterator it  = edges.iterator();
        while (it.hasNext()){
            MyEdge edge = (MyEdge) it.next();
            sum += edge.getA()/(edge.getC()/m - edge.getA());
        }
        sum /= g;

        return sum;
    }


    private void loadData(){
        String message = "";
        String line;
        try{
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null)
                message += line;

            Gson gson = new Gson();
            MyGraph graph = gson.fromJson(message,MyGraph.class);

            this.vertices = graph.getVertices();
            this.edges = graph.getEdges();
            this.matrix = graph.getMatrix();

        }catch (IOException e){
            System.out.println("Nie udalo sie wczygac pliku!!!");
            System.out.println(e.getMessage());
            System.exit(0);
        }catch (JsonSyntaxException e){
            System.out.println("Zly format json");
            System.exit(0);
        }
    }
    public MyGraph(String path) {
        this.path = path;
        loadData();
    }

    private List<String> getVertices() {
        return vertices;
    }
    public List<MyEdge> getEdges() {
        return edges;
    }
    public List<List<Integer>> getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        return "MyGraph{" +
                "vertices=" + vertices +
                ", edges=" + edges +
                '}';
    }
}

class MyEdge{

    String vertex1;
    String vertex2;

    double reliability;

    private transient double a;
    private transient double c;

    @Override
    public String toString() {
        return "MyEdge{" +
                "vertex1='" + vertex1 + '\'' +
                ", vertex2='" + vertex2 + '\'' +
                ", reliability=" + reliability +
                ", a=" + a +
                ", c=" + c +
                '}';
    }

    public MyEdge(String vertex1, String vertex2, double reliability) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.reliability = reliability;
        a = c = 0;
    }

    public MyEdge(String vertex1, String vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        reliability = 0;
        a = 0;
        c = 0;

    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyEdge)) return false;

        MyEdge myEdge = (MyEdge) o;

        if((myEdge.vertex1.equals(vertex1) && myEdge.vertex2.equals(vertex2)) ||
                (myEdge.vertex1.equals(vertex2) && myEdge.vertex2.equals(vertex1)) )
            return true;
        else
            return false;
    }


}
