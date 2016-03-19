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
    Response resp = this.engine.process(req);

    assertThat(resp.statusCode()).isEqualTo(StatusCode.NOT_FOUND);
  }

  @Test
  public void get_emptyOptional_404() throws InterruptedException, ExecutionException {
    Request req = new Request("/empty", HttpMethod.GET);
    Response resp = this.engine.process(req);

    assertThat(resp.statusCode()).isEqualTo(StatusCode.NOT_FOUND);
  }

  @Test
  public void get_presentOptional_200() throws InterruptedException, ExecutionException {
    Request req = new Request("/present", HttpMethod.GET);
    Response resp = this.engine.process(req);

    assertThat(resp.statusCode()).isEqualTo(StatusCode.OK);
  }

  @Test
  public void get_200_responseBodyIsPresent() throws InterruptedException, ExecutionException {
    Request req = new Request("/present", HttpMethod.GET);
    Response resp = this.engine.process(req);

    assertThat(resp.body()).isPresent();
  }

  @Test
  public void get_200_responseBodyMessage() throws InterruptedException, ExecutionException {
    Request req = new Request("/present", HttpMethod.GET);
    Response resp = this.engine.process(req);

    Message message = fromJson(resp.body().get(), Message.class);
    assertThat(message.content).isEqualTo("Hello, world!");
  }

  @Test
  public void get_200FromResponseHandler_responseBodyMessage() throws InterruptedException, ExecutionException {
    Request req = new Request("/message_response", HttpMethod.GET);
    Response resp = this.engine.process(req);

    Message message = fromJson(resp.body().get(), Message.class);
    assertThat(message.content).isEqualTo("Hello, world!");
  }

  @Test
  public void get_200FromObjectHandler_responseBodyMessage() throws InterruptedException, ExecutionException {
    Request req = new Request("/message", HttpMethod.GET);
    Response resp = this.engine.process(req);

    Message message = fromJson(resp.body().get(), Message.class);
    assertThat(message.content).isEqualTo("Hello, world!");
  }

  @Test
  public void get_handlerThrowsException_500() throws InterruptedException, ExecutionException {
    Request req = new Request("/error", HttpMethod.GET);
    Response resp = this.engine.process(req);

    assertThat(resp.statusCode()).isEqualTo(StatusCode.SERVER_ERROR);
  }

  @Test
  public void get_dynamicUrl_requestHasAttribute() throws InterruptedException, ExecutionException {
    Request req = new Request("/dynamic/1/other/2", HttpMethod.GET);
    Response resp = this.engine.process(req);

    Message message = fromJson(resp.body().get(), Message.class);
    assertThat(message.content).isEqualTo("1:2");
  }

  @Test
  public void get_auth_without_header_401() throws InterruptedException, ExecutionException {
    Request req = new Request("/filtered/me", HttpMethod.GET);
    Response resp = this.engine.process(req);

    assertThat(resp.statusCode()).isEqualTo(StatusCode.UNAUTHORIZED);
  }

  @Test
  public void get_auth_with_header_200() throws InterruptedException, ExecutionException {
    Request req = new Request("/filtered/me", HttpMethod.GET);
    req.header("auth", "secret");
    Response resp = this.engine.process(req);

    assertThat(resp.statusCode()).isEqualTo(StatusCode.OK);
  }
}
