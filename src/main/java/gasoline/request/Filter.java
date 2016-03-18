package gasoline.request;

import java.util.Arrays;
import java.util.List;

import gasoline.engine.routing.Route;

public class Filter {

  public final List<Route> routes;
  public final FilterHandler handler;

  public Filter(FilterHandler filter, Route... routes) {
    this(filter, Arrays.asList(routes));
  }
  
  public Filter(FilterHandler filter, List<Route> routes) {
    this.handler = filter;
    this.routes = routes;
  }
}