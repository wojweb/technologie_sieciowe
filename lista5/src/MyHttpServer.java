
import java.io.*;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MyHttpServer {
    public static void main(String[] args) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(8080);
        HttpServer server = HttpServer.create(addr, 0);

        server.createContext("/", new MyHandler());
        server.createContext("/index.html", new IndexHandler());
        server.createContext("/subpage.html", new SubPageHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is listening on port 8080" );
    }
}

class IndexHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod);
        String response = "";
        FileReader fr = new FileReader("/home/pan/Studia/Semestr4/sieci/lista5/index.html");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while(line != null){
            System.out.println(line);
            response+= line;
            line = br.readLine();
        }

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
class SubPageHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod);
        String response = "";
        FileReader fr = new FileReader("/home/pan/Studia/Semestr4/sieci/lista5/subpage.html");
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while(line != null){
            System.out.println(line);
            response+= line;
            line = br.readLine();
        }

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}

class MyHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {

//        String cos = exchange.getRequestHeaders().toString();
//        System.out.println(cos);

        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            Headers requestHeaders = exchange.getRequestHeaders();
            Set<String> keySet = requestHeaders.keySet();
            Iterator<String> iter = keySet.iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                List values = requestHeaders.get(key);
                String s = key + " = " + values.toString() + "\n";
                responseBody.write(s.getBytes());
            }
            responseBody.close();
        }
    }
}