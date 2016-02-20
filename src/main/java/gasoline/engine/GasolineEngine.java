package gasoline.engine;

import static gasoline.Context.notFound;
import static gasoline.Context.ok;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gasoline.engine.routing.PathUtils;
import gasoline.engine.routing.Route;
import gasoline.engine.routing.RoutingTable;
import gasoline.http.StatusCode;
import gasoline.request.FilterHandler;
import gasoline.request.Request;
import gasoline.response.Response;
import gasoline.utils.Log;

public class GasolineEngine {

  private final RoutingTable routingTable;
  private final FilterResolver filters;

  public GasolineEngine(RoutingTable routingTable, FilterResolver filterResolver) {
    this.routingTable = routingTable;
    this.filters = filterResolver;
  }

  /**
   * Processes the given request and returns a response.
   *
   * This does most of the request process life-cycle, from routing the request,
   * processing it, encoding the response when needed and so on.
   */
  public Response process(Request request) {
    // TODO set log context
    Response resp;
    Optional<Route> route = this.routingTable.findRouteFor(request.path(), request.method());
    try {
      if (route.isPresent()) {
        Route r = route.get();
        this.prepareRequest(request, r);
        this.executeFilters(request, r);
        resp = this.executeRoute(request, r);
        Log.info("Request for {} processed. Status code: {}", route.get().path, resp.statusCode());
      } else {
        resp = notFound();
        Log.info("Route for {} not found", request);
      }
    } catch (RequestAbortedException ex) {
      resp = new Response(ex.statusCode);
    } catch (Throwable t) {
      Log.error(t, "Internal Server error when processing request for {}", route.get());
      resp = new Response(StatusCode.SERVER_ERROR);
    }
    return resp;
  }

  private void executeFilters(Request request, Route route) {
    List<FilterHandler> filters = this.filters.findFiltersFor(route);
    filters.forEach((handler) -> {
      Log.debug("Executing filters for route {}", route);
      handler.filter(request);
    });
  }

  private void prepareRequest(Request request, Route route) {
    Pattern p = Pattern.compile(PathUtils.dynamicPathRegex(route.path));
    Matcher m = p.matcher(request.path());
    if (m.matches()) {
      for (int i = 0; i < route.attributes.size(); i++) {
        request.attribute(route.attributes.get(i), m.group(i + 1));
      }
    }
  }

  private Response executeRoute(Request request, Route route) {
    Log.debug("Executing handler for {}", route.path);
    Object result = route.handler.handle(request);
    return this.processResult(result, route);
  }

  private Response processResult(Object result, Route route) {
    Log.debug("Processing response {} for {}", result, route.path);
    Response resp = null;
    if (result instanceof Optional) {
      Optional<?> opt = (Optional<?>) result;
      if (opt.isPresent()) {
        resp = (Response) ok(opt.get());
      } else {
        resp = (Response) notFound();
      }
    } else if (result instanceof Response) {
      resp = (Response) result;
    } else {
      // TODO future and Completable Futures
      resp = new Response(StatusCode.OK, result);
    }

    return resp;
  }
}
