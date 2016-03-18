/**
 * Gasoline  Copyright (C) 2015  daniloqueiroz.github.io/gasoline
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        Log.info("Dynamic route {} recorded", route);
        this.dynamicRoutes.insert(dynamicPathRegex(route.path), route);
      } else {
        Log.info("Static route {} recorded", route);
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
    Log.debug("Trying to find route for {}", routeKey);
    Route route = this.staticRoutes.getOrDefault(routeKey, this.dynamicRoutes.search(path, method));
    Optional<Route> result = Optional.ofNullable(route);
    Log.debug("Found route for {} was {}", routeKey, result);
    return result;
  }
}
