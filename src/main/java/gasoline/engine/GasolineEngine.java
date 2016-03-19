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
package gasoline.engine;

import static gasoline.Context.notFound;
import static gasoline.Context.ok;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gasoline.engine.routing.PathUtils;
import gasoline.engine.routing.Route;
import gasoline.engine.routing.RoutingTable;
import gasoline.http.StatusCode;
import gasoline.request.FilterHandler;
import gasoline.request.Request;
import gasoline.response.Response;

public class GasolineEngine {

  private static final Logger LOG = LoggerFactory.getLogger(GasolineEngine.class);
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
        LOG.info("Request for {} processed. Status code: {}", route.get().path, resp.statusCode());
      } else {
        resp = notFound();
        LOG.info("Route for {} not found", request);
      }
    } catch (RequestAbortedException ex) {
      resp = new Response(ex.statusCode);
    } catch (Throwable t) {
      LOG.error("Internal Server error when processing request for {}", route.get());
      LOG.error("Internal Server error when processing request", t);
      resp = new Response(StatusCode.SERVER_ERROR);
    }
    return resp;
  }

  private void executeFilters(Request request, Route route) {
    List<FilterHandler> filters = this.filters.findFiltersFor(route);
    filters.forEach((handler) -> {
      LOG.debug("Executing filters for route {}", route);
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
    LOG.debug("Executing handler for {}", route.path);
    Object result = route.handler.handle(request);
    return this.processResult(result, route);
  }

  private Response processResult(Object result, Route route) {
    LOG.debug("Processing response {} for {}", result, route.path);
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
