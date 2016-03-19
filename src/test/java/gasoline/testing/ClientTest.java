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

import static gasoline.utils.Pair.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import gasoline.http.StatusCode;
import gasoline.response.Response;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class ClientTest {

  Client client = new Client(new TestingApp()::init);

  @Test
  public void get_noRoute_404() {
    Response resp = this.client.get("/not_found");
    assertThat(resp.statusCode()).isEqualTo(StatusCode.NOT_FOUND);
  }

  @Test
  public void get_withHeaders_200() {
    Map<String, String> headers = pairs(p("auth", "secret")).asMap();
    Response resp = this.client.get("/filtered/me", headers);
    assertThat(resp.statusCode()).isEqualTo(StatusCode.OK);
  }

}
