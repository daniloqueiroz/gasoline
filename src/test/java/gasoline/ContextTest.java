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
package gasoline;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import gasoline.http.StatusCode;
import gasoline.response.Response;
import gasoline.testing.TestingApp.Message;

public class ContextTest {

  @Test
  public void toJson_fromJson() {
    String content = "Hey na na!";
    String json = Context.toJson(new Message(content));
    Message msg = Context.fromJson(json, Message.class);

    assertThat(msg.content).isEqualTo(content);
  }

  @Test
  public void toJson_fromJsonResponse() {
    String content = "Hey na na!";
    Response resp = new Response(StatusCode.OK, new Message(content));

    Message msg = Context.fromJson(resp.body().get(), Message.class);

    assertThat(msg.content).isEqualTo(content);
  }
}
