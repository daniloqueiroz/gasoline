package gasoline.testing;

import static gasoline.Context.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import gasoline.Application;
import gasoline.Module;
import gasoline.engine.GasolineEngine;
import gasoline.http.HttpMethod;
import gasoline.request.Request;
import gasoline.response.Response;
import gasoline.response.ResponseFuture;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class Client {

  private final GasolineEngine engine;

  public Client(Module module) {
    Application app = new Application(module);
    this.engine = app.engine();
  }

  private Response request(Request request) {
    ResponseFuture future = new ResponseFuture();
    this.engine.handle(request, future);
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  public Response get(String urlPath) {
    return this.request(new Request(urlPath, HttpMethod.GET));

  }

  public Response post(String urlPath) {
    return this.request(new Request(urlPath, HttpMethod.POST));
  }

  public Response post(String urlPath, Object body) {
    return this.request(new Request(urlPath, HttpMethod.POST, toJson(body)));
  }
}
