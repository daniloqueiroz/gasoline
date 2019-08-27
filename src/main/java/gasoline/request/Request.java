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
package gasoline.request;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Optional;

import gasoline.engine.routing.PathUtils;
import gasoline.http.HttpMethod;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class Request {

  private final String path;
  private final HttpMethod method;
  private final HashMap<String, String> attributes = new HashMap<>();
  private final HashMap<String, String> headers = new HashMap<>();
  private final HashMap<String, String> parameters = new HashMap<>();
  private String body;

  public Request(String urlPath, HttpMethod method) {
    this.path = PathUtils.normalizePath(urlPath);
    this.method = method;
  }

  public Request(String urlPath, HttpMethod method, String jsonBody) {
    this(urlPath, method);
    this.body = jsonBody;
  }

  public String path() {
    return this.path;
  }

  public HttpMethod method() {
    return this.method;
  }

  public Optional<String> body() {
    return Optional.ofNullable(this.body);
  }

  public String attribute(String name) {
    return this.attributes.get(name);
  }

  public void attribute(String name, String value) {
    this.attributes.put(name, value);
  }

  public Optional<String> header(String name) {
    return Optional.ofNullable(this.headers.get(name));
  }

  public void header(String name, String value) {
    this.headers.put(name, value);
  }

  public Optional<String> parameter(String name) {
    return Optional.ofNullable(this.parameters.get(name));
  }

  public void parameter(String name, String value) {
    this.parameters.put(name, value);
  }

  @Override
  public String toString() {
    String body = this.body().isPresent()? this.body().get()  :"EMPTY BODY";
    return format("[%s %s:  %s]", this.method, this.path, body);
  }
}
