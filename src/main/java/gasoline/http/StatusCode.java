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
package gasoline.http;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public enum StatusCode {
  OK(200), CREATED(201), BAD_REQUEST(400), UNAUTHORIZED(401),
  NOT_FOUND(404), FORBIDEEN(403), SERVER_ERROR(500);

  public int code;

  StatusCode(int code) {
    this.code = code;
  }
}
