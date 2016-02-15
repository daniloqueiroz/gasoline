package gasoline.engine;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gasoline.engine.routing.Route;
import gasoline.request.Filter;
import gasoline.request.FilterHandler;

public class FilterResolver {

  private final Map<Route, List<FilterHandler>> filtersByRoute = new HashMap<>();

  public FilterResolver(List<Filter> filters) {
    filters.forEach((filter) -> {
      filter.routes.forEach((route) -> {
        List<FilterHandler> handlers = this.filtersByRoute.getOrDefault(route, new LinkedList<>());
        handlers.add(filter.handler);
        this.filtersByRoute.put(route, handlers);
      });
    });
  }

  public List<FilterHandler> findFiltersFor(Route route) {
    return this.filtersByRoute.getOrDefault(route, emptyList());
  }

}
