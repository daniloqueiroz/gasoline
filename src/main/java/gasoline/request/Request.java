package gasoline.request;

import java.util.HashMap;
import java.util.Optional;

import gasoline.engine.routing.PathUtils;
import gasoline.http.HttpMethod;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class Request {

  private final String path;
  private final HttpMethod method;
  private final HashMap<String, String> attributes = new HashMap<>();
  private final HashMap<String, String> headers = new HashMap<>();
  private String body;

  public Request(String urlPath, HttpMethod method) {
    this.path = PathUtils.normalizePath(urlPath);
    this.method = method;
  }

  public Request(String urlPath, HttpMethod method, String jsonBody) {
    this(urlPath, method);
    this.body = jsonBody;
  }

  public String path() {
    return this.path;
  }

  public HttpMethod method() {
    return this.method;
  }

  public Optional<String> body() {
    return Optional.ofNullable(this.body);
  }

  public String attribute(String name) {
    return this.attributes.get(name);
  }

  public void attribute(String name, String value) {
    this.attributes.put(name, value);
  }

  public Optional<String> header(String name) {
    return Optional.ofNullable(this.headers.get(name));
  }

  public void header(String name, String value) {
    this.headers.put(name, value);
  }
}
