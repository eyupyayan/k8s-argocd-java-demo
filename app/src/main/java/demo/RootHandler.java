package demo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class RootHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String greeting = env("GREETING", "Hello from Kubernetes");
    String secretHint = env("API_TOKEN", "(no secret mounted)");

    String body = ""
        + greeting + "\n"
        + "time=" + Instant.now() + "\n"
        + "api_token_present=" + (!"(no secret mounted)".equals(secretHint)) + "\n";

    byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
    exchange.sendResponseHeaders(200, bytes.length);
    exchange.getResponseBody().write(bytes);
    exchange.close();
  }

  private static String env(String key, String def) {
    String v = System.getenv(key);
    return (v == null || v.isBlank()) ? def : v;
  }
}
