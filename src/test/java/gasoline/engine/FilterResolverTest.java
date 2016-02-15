package gasoline.engine;

import static gasoline.Context.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import gasoline.engine.routing.Route;
import gasoline.http.HttpMethod;
import gasoline.http.StatusCode;
import gasoline.request.Filter;
import gasoline.request.FilterHandler;

public class FilterResolverTest {

  Route route1 = new Route("/lala", HttpMethod.GET, (req)-> "Hello");
  Route route2 = new Route("/lolo", HttpMethod.GET, (req)-> {
    return abort(StatusCode.SERVER_ERROR);
  });

  @Test
  public void find_filter_inexistent_route_empty() {
    FilterResolver resolver = new FilterResolver(emptyList());
    assertThat(resolver.findFiltersFor(route1)).isEmpty();
  }

  @Test
  public void find_filter_existent_route_ok() {
    FilterHandler handler = (req) -> {
      ;
    };
    Filter f = new Filter(handler, route1, route2);
    FilterResolver resolver = new FilterResolver(asList(f));
    List<FilterHandler> filters = resolver.findFiltersFor(route1);
    assertThat(filters).hasSize(1);
    assertThat(filters.get(0)).isEqualTo(handler);
  }

  @Test
  public void find_filter_multiple_routes_ok() {
    Filter f = new Filter((req) -> {;}, route1, route2);
    FilterResolver resolver = new FilterResolver(asList(f));
    FilterHandler filter1 = resolver.findFiltersFor(route1).get(0);
    FilterHandler filter2 = resolver.findFiltersFor(route2).get(0);
    assertThat(filter1).isEqualTo(filter2);
  }
}
