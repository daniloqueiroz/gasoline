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
package gasoline.response;

import java.util.Optional;

import gasoline.Context;
import gasoline.http.StatusCode;

public class Response {
  private final StatusCode code;
  private Optional<String> body;

  public Response(StatusCode code) {
    this(code, null);
  }

  public Response(StatusCode code, Object content) {
    this.code = code;
    this.prepareBody(content);
  }

  public Optional<String> body() {
    return this.body;
  }

  public StatusCode statusCode() {
    return this.code;
  }

  public void prepareBody(Object content) {
    if (content != null) {
      String rawBody = Context.toJson(content);
      this.body = Optional.ofNullable(rawBody);
    } else {
      this.body = Optional.empty();
    }
  }
}
