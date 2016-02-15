package gasoline;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import gasoline.engine.FilterResolver;
import gasoline.engine.GasolineEngine;
import gasoline.engine.Server;
import gasoline.engine.routing.Route;
import gasoline.engine.routing.RoutingTable;
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

  public GasolineEngine engine() {
    return new GasolineEngine(new RoutingTable(this.routes), new FilterResolver(this.filters));
  }

  public Server server() {
    return new Server();
  }
}
