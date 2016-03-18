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
package gasoline;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import gasoline.engine.FilterResolver;
import gasoline.engine.GasolineEngine;
import gasoline.engine.routing.Route;
import gasoline.engine.routing.RoutingTable;
import gasoline.engine.server.JettyServer;
import gasoline.http.HttpMethod;
import gasoline.request.Filter;
import gasoline.request.FilterHandler;
import gasoline.request.RequestHandler;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class Application {

  private final LinkedHashSet<Route> routes = new LinkedHashSet<>();
  private final List<Filter> filters = new LinkedList<>();
  private final List<FilterHandler> globalFilters = new LinkedList<>();

  /**
   * Create an Application with the given modules
   */
  public Application(Module... modules) {
    for (Module m : modules) {
      this.load(m);
    }
  }

  /**
   * Load a new module for this application
   */
  public Application load(Module module) {
    module.init(this);
    return this;
  }

  /**
   * Adds a new Route to the application.
   *
   * <pre>
   * application.route("/hello/{name}", HttpMethod.GET, (req) -> {
   *   return String.format("Hello, %s!", req.attribute("name"));
   * });
   * </pre>
   *
   * @param path
   *          The url path for the route. It should start with a '/' and can
   *          contain path attributes defined using curly-braces. Eg.:
   *          /resources/{name}/subresources/{id}/
   * @param method
   *          The {@link HttpMethod} to be used
   * @param handler
   *          a {@link RequestHandler}. As it's a functional interface, it can
   *          be defined as a lambda function.
   * @return The added {@link Route}.
   */
  public Route route(String path, HttpMethod method, RequestHandler handler) {
    Route r = new Route(path, method, handler);
    this.routes.add(r);
    return r;
  }

  /**
   * Adds a {@link HttpMethod#GET} route.
   *
   * @see Application#route(String, HttpMethod, RequestHandler)
   */
  public Route get(String path, RequestHandler handler) {
    return this.route(path, HttpMethod.GET, handler);
  }

  /**
   * Adds a {@link HttpMethod#POST} route.
   *
   * @see Application#route(String, HttpMethod, RequestHandler)
   */
  public Route post(String path, RequestHandler handler) {
    return this.route(path, HttpMethod.POST, handler);
  }

  /**
   * Adds a {@link HttpMethod#PUT} route.
   *
   * @see Application#route(String, HttpMethod, RequestHandler)
   */
  public Route put(String path, RequestHandler handler) {
    return this.route(path, HttpMethod.PUT, handler);
  }

  /**
   * Adds a {@link HttpMethod#DELETE} route.
   *
   * @see Application#route(String, HttpMethod, RequestHandler)
   */
  public Route delete(String path, RequestHandler handler) {
    return this.route(path, HttpMethod.DELETE, handler);
  }

  /**
   * Adds a {@link HttpMethod#PATCH} route.
   *
   * @see Application#route(String, HttpMethod, RequestHandler)
   */
  public Route patch(String path, RequestHandler handler) {
    return this.route(path, HttpMethod.PATCH, handler);
  }

  /**
   * Adds a {@link FilterHandler} to be executed to filter the request
   * <b>BEFORE</b> the execution of the given routes.
   *
   * <pre>
   * application.before((req) -> {
   *        // TODO do something before the RequestHandler.
   *    },
   *    application.get(...);
   * );
   * </pre>
   *
   * @param filter
   *          The {@link FilterHandler} to filter the requests. As it's a
   *          functional interface, it can be defined as a lambda function.
   * @param routes
   *          The Routes which this filter must be applied.
   */
  public void before(FilterHandler filter, Route... routes) {
    this.filters.add(new Filter(filter, routes));
  }

  /**
   * Adds a {@link FilterHandler} to be executed to filter the request
   * <b>BEFORE</b> the execution of ALL routes.
   *
   * <pre>
   * application.beforeAll((req) -> {
   *        // TODO do something before the RequestHandler.
   *    });
   * </pre>
   *
   * @param filter
   *          The {@link FilterHandler} to filter all requests. As it's a
   *          functional interface, it can be defined as a lambda function.
   */
  public void beforeAll(FilterHandler filter) {
    this.globalFilters.add(filter);
  }
  
  public GasolineEngine engine() {
    this.globalFilters.forEach(handler -> {
      this.filters.add(new Filter(handler, new ArrayList<>(this.routes)));
    });
    return new GasolineEngine(new RoutingTable(this.routes), new FilterResolver(this.filters));
  }

  public JettyServer server() {
    return new JettyServer(this.engine());
  }
}
