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
package gasoline.testing;

import static gasoline.Context.toJson;

import java.util.Map;
import java.util.Optional;

import gasoline.Application;
import gasoline.Module;
import gasoline.engine.GasolineEngine;
import gasoline.http.HttpMethod;
import gasoline.request.Request;
import gasoline.response.Response;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class Client {

  private final GasolineEngine engine;

  public Client(Module module) {
    Application app = new Application(module);
    this.engine = app.engine();
  }

  private Response request(String urlPath, HttpMethod method) {
    return this.request(urlPath, method, Optional.empty(), Optional.empty());
  }

  private Response request(String urlPath, HttpMethod method, Optional<Object> body,
      Optional<Map<String, String>> headers) {
    return dispatchRequest(createRequest(urlPath, method, body, headers));
  }

  private Response dispatchRequest(Request request) {
    // TODO handle future/completableFutures
    return this.engine.process(request);
  }

  private Request createRequest(String urlPath, HttpMethod method, Optional<Object> body,
      Optional<Map<String, String>> headers) {
    Request request;
    if (body.isPresent()) {
      request = new Request(urlPath, method, toJson(body.get()));
    } else {
      request = new Request(urlPath, method);
    }
    headers.ifPresent((map) -> map.entrySet().forEach((entry) -> {
      request.header(entry.getKey(), entry.getValue());
    }));
    return request;
  }

  public Response get(String urlPath) {
    return this.request(urlPath, HttpMethod.GET);
  }

  public Response get(String urlPath, Map<String, String> headers) {
    return this.request(urlPath, HttpMethod.GET, Optional.empty(), Optional.ofNullable(headers));
  }

  public Response post(String urlPath) {
    return this.request(urlPath, HttpMethod.POST);
  }

  public Response post(String urlPath, Object body) {
    return this.request(urlPath, HttpMethod.POST, Optional.ofNullable(body), Optional.empty());
  }

  public Response put(String urlPath) {
    return this.request(urlPath, HttpMethod.PUT);
  }

  public Response put(String urlPath, Object body) {
    return this.request(urlPath, HttpMethod.PUT, Optional.ofNullable(body), Optional.empty());
  }

  public Response delete(String urlPath) {
    return this.request(urlPath, HttpMethod.DELETE);
  }
}
