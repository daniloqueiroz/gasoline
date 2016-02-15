package gasoline.request;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import gasoline.Context;
import gasoline.response.Response;

/**
 * A functional interface that handles a given request.
 * 
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
@FunctionalInterface
public interface RequestHandler {

  /**
   * Handles a request. This method can return anything, from
   * a {@link Response} object (see builder methods on {@link Context} class),
   * to an Object, or a {@link Future} or {@link CompletableFuture}.
   */
  public Object handle(Request req);
}
