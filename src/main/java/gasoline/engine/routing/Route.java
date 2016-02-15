package gasoline.engine.routing;

import static gasoline.engine.routing.PathUtils.getAttributesNames;
import static gasoline.engine.routing.PathUtils.normalizePath;
import static java.util.Objects.hash;

import java.util.List;

import gasoline.http.HttpMethod;
import gasoline.request.RequestHandler;

/**
 * A gasoline Route.
 *
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class Route {

  public final String path;
  public final HttpMethod method;
  public final RequestHandler handler;
  public final List<String> attributes;

  public Route(String path, HttpMethod method, RequestHandler handler) {
    this.path = normalizePath(path);
    this.method = method;
    this.handler = handler;
    this.attributes = getAttributesNames(path);
  }

  @Override
  public String toString() {
    return "Route [path=" + this.path + ", method=" + this.method + "]";
  }

  @Override
  public int hashCode() {
    return hash(this.path, this.method);
  }

  @Override
  public boolean equals(Object obj) {
    boolean equals = false;
    if (obj instanceof Route) {
      Route other = (Route) obj;
      equals = this.path.equals(other.path) && this.method.equals(other.method);
    }
    return equals;
  }
}
