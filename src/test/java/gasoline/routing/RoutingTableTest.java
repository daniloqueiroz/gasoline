package gasoline.routing;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;

import gasoline.engine.routing.Route;
import gasoline.engine.routing.RoutingTable;
import gasoline.http.HttpMethod;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class RoutingTableTest {

  @Test
  public void find_unregisteredRoute_routeNotFound() {
    RoutingTable router = new RoutingTable(Collections.emptySet());
    Optional<Route> route = router.findRouteFor("/users", HttpMethod.GET);

    assertThat(route).isEmpty();
  }

  @Test
  public void find_wrongMethod_routeNotFound() {
    RoutingTable router = new RoutingTable(asList(new Route("/", HttpMethod.POST, (req) -> {
      return null;
    })));
    Optional<Route> route = router.findRouteFor("/", HttpMethod.GET);

    assertThat(route).isEmpty();
  }

  @Test
  public void find_variableRouteWrongMethod_routeNotFound() {
    RoutingTable router = new RoutingTable(asList(new Route("/users/{name}/posts/{id}/",
        HttpMethod.POST, (req) -> {
      return null;
    })));
    Optional<Route> route = router.findRouteFor("/users/danilo/posts/1/", HttpMethod.GET);

    assertThat(route).isEmpty();
  }

  @Test
  public void find_simpleVariableRouteWrongPath_routeNotFound() {
    RoutingTable router = new RoutingTable(asList(new Route("/{name}/messages",
         HttpMethod.GET, (req) -> {
      return null;
    })));
    Optional<Route> route = router.findRouteFor("/danilo", HttpMethod.GET);

    assertThat(route).isEmpty();
  }

  @Test
  public void find_variableRoute_returnRoute() {
    Route originalRoute = new Route("/users/{name}/posts/{id}/", HttpMethod.GET, (req) -> {
      return null;
    });

    RoutingTable router = new RoutingTable(asList(originalRoute));
    Optional<Route> route = router.findRouteFor("/users/danilo/posts/1/", HttpMethod.GET);

    assertThat(route).containsSame(originalRoute);
  }

  @Test
  public void find_simpleVariableRoute_returnRoute() {
    Route originalRoute = new Route("/{name}", HttpMethod.GET, (req) -> {
      return null;
    });

    RoutingTable router = new RoutingTable(asList(originalRoute));
    Optional<Route> route = router.findRouteFor("/danilo", HttpMethod.GET);

    assertThat(route).containsSame(originalRoute);
  }

  @Test
  public void find_multipleVariableRoutes_returnRoute() {
    Route originalRoute = new Route("/users/{name}/posts/{id}/", HttpMethod.GET, (req) -> {
      return null;
    });

    RoutingTable router = new RoutingTable(asList(originalRoute));
    Optional<Route> route = router.findRouteFor("/users/danilo/posts/1", HttpMethod.GET);
    
    assertThat(route).containsSame(originalRoute);
  }
}
