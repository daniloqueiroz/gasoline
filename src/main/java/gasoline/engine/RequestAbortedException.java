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

import static java.lang.String.format;

import gasoline.http.StatusCode;

public class RequestAbortedException extends RuntimeException {

  private static final long serialVersionUID = -3512806574950243766L;
  public final StatusCode statusCode;

  public RequestAbortedException(StatusCode code) {
    super(format("Request aborted! Status Code: %s", code));
    this.statusCode = code;
  }

}
