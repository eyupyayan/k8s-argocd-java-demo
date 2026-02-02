package demo;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
  public static void main(String[] args) throws IOException {
    int port = Integer.parseInt(env("PORT", "8080"));

    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/", new RootHandler());
    server.createContext("/healthz", exchange -> {
      byte[] body = "ok\n".getBytes();
      exchange.sendResponseHeaders(200, body.length);
      exchange.getResponseBody().write(body);
      exchange.close();
    });

    server.start();
    System.out.println("Listening on port " + port);
  }

  private static String env(String key, String def) {
    String v = System.getenv(key);
    return (v == null || v.isBlank()) ? def : v;
  }
}
