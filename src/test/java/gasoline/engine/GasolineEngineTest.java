package gasoline.engine;

import static gasoline.Context.fromJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import gasoline.Application;
import gasoline.http.HttpMethod;
import gasoline.http.StatusCode;
import gasoline.request.Request;
import gasoline.response.Response;
import gasoline.response.ResponseFuture;
import gasoline.testing.TestingApp;
import gasoline.testing.TestingApp.Message;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class GasolineEngineTest {
  private GasolineEngine engine;

  @Before
  public void setUp() {
    this.engine = new Application(new TestingApp()::init).engine();
  }

  @Test
  public void handle_urlNotFound_404() throws InterruptedException, ExecutionException {
    Request req = new Request("/not_found", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    assertThat(holder.get().statusCode()).isEqualTo(StatusCode.NOT_FOUND);
  }

  @Test
  public void get_emptyOptional_404() throws InterruptedException, ExecutionException {
    Request req = new Request("/empty", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    assertThat(holder.get().statusCode()).isEqualTo(StatusCode.NOT_FOUND);
  }

  @Test
  public void get_presentOptional_200() throws InterruptedException, ExecutionException {
    Request req = new Request("/present", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    assertThat(holder.get().statusCode()).isEqualTo(StatusCode.OK);
  }

  @Test
  public void get_200_responseBodyIsPresent() throws InterruptedException, ExecutionException {
    Request req = new Request("/present", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    Response resp = holder.get();
    assertThat(resp.body()).isPresent();
  }

  @Test
  public void get_200_responseBodyMessage() throws InterruptedException, ExecutionException {
    Request req = new Request("/present", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    Response resp = holder.get();
    Message message = fromJson(resp.body().get(), Message.class);
    assertThat(message.content).isEqualTo("Hello, world!");
  }

  @Test
  public void get_200FromResponseHandler_responseBodyMessage() throws InterruptedException, ExecutionException {
    Request req = new Request("/message_response", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    Response resp = holder.get();
    Message message = fromJson(resp.body().get(), Message.class);
    assertThat(message.content).isEqualTo("Hello, world!");
  }

  @Test
  public void get_200FromObjectHandler_responseBodyMessage() throws InterruptedException, ExecutionException {
    Request req = new Request("/message", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    Response resp = holder.get();
    Message message = fromJson(resp.body().get(), Message.class);
    assertThat(message.content).isEqualTo("Hello, world!");
  }

  @Test
  public void get_handlerThrowsException_500() throws InterruptedException, ExecutionException {
    Request req = new Request("/error", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    assertThat(holder.get().statusCode()).isEqualTo(StatusCode.SERVER_ERROR);
  }

  @Test
  public void get_dynamicUrl_requestHasAttribute() throws InterruptedException, ExecutionException {
    Request req = new Request("/dynamic/1/other/2", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    Response resp = holder.get();
    Message message = fromJson(resp.body().get(), Message.class);
    assertThat(message.content).isEqualTo("1:2");
  }

  @Test
  public void get_auth_without_header_401() throws InterruptedException, ExecutionException {
    Request req = new Request("/filtered/me", HttpMethod.GET);
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    Response resp = holder.get();
    assertThat(resp.statusCode()).isEqualTo(StatusCode.UNAUTHORIZED);
  }

  @Test
  public void get_auth_with_header_200() throws InterruptedException, ExecutionException {
    Request req = new Request("/filtered/me", HttpMethod.GET);
    req.header("auth", "secret");
    ResponseFuture holder = new ResponseFuture();
    this.engine.handle(req, holder);

    Response resp = holder.get();
    assertThat(resp.statusCode()).isEqualTo(StatusCode.OK);
  }
}
