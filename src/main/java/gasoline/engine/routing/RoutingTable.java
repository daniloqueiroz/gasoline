package gasoline.engine.routing;

import static gasoline.engine.routing.PathUtils.dynamicPathRegex;
import static gasoline.engine.routing.PathUtils.isDynamicPath;
import static java.lang.String.format;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import gasoline.http.HttpMethod;
import gasoline.utils.Log;

/**
 * This entity is responsible to find a {@link Route} for a given URL path.
 *
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class RoutingTable {

  private final Map<String, Route> staticRoutes = new HashMap<>();
  private final PathTrie dynamicRoutes = new PathTrie();

  public RoutingTable(Collection<Route> routes) {
    this.buildRoutingTables(routes);
  }

  private RoutingTable buildRoutingTables(Collection<Route> routes) {
    for (Route route : routes) {
      if (isDynamicPath(route.path)) {
        Log.info("Dynamic route %s recorded", route);
        this.dynamicRoutes.insert(dynamicPathRegex(route.path), route);
      } else {
        Log.info("Static route %s recorded", route);
        this.staticRoutes.put(this.routeKey(route), route);
      }
    }
    return this;
  }

  private String routeKey(Route route) {
    return this.routeKey(route.path, route.method);
  }

  private String routeKey(String path, HttpMethod method) {
    return format("%s:%s", method.name(), path);
  }

  /**
   * Finds an route for the given path and {@link HttpMethod}
   */
  public Optional<Route> findRouteFor(String path, HttpMethod method) {
    String routeKey = this.routeKey(path, method);
    Log.debug("Trying to find route for %s", routeKey);
    Route route = this.staticRoutes.getOrDefault(routeKey, this.dynamicRoutes.search(path, method));
    Optional<Route> result = Optional.ofNullable(route);
    Log.debug("Found route for %s was %s", routeKey, result);
    return result;
  }
}
