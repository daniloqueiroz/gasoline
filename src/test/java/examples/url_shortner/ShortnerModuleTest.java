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
package examples.url_shortner;

import static gasoline.Context.fromJson;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import examples.url_shortner.ShortnerModule.ShortUrl;
import examples.url_shortner.ShortnerModule.ShortUrlRequest;
import gasoline.http.StatusCode;
import gasoline.response.Response;
import gasoline.testing.Client;

public class ShortnerModuleTest {

  private ShortnerModule shortner;
  private Client client;

  @Before
  public void setUp() {
    this.shortner = new ShortnerModule();
    this.client = new Client(this.shortner);
  }

  @Test
  public void expand_inexistent_url_returns_404() {
    Response resp = this.client.get("/inexistent");
    assertThat(resp.statusCode()).isEqualTo(StatusCode.NOT_FOUND);
  }

  @Test
  public void create_short_url_returns_201() {
    String url = "http://google.com";
    ShortUrlRequest request = new ShortUrlRequest(url);

    Response resp = this.client.post("/", request);
    ShortUrl shortUrl = fromJson(resp.body().get(), ShortUrl.class);

    assertThat(resp.statusCode()).isEqualTo(StatusCode.CREATED);
    assertThat(shortUrl.url).isEqualTo(url);
    assertThat(shortUrl.short_url).isNotEmpty();
  }

  @Test
  public void create_short_url_no_body_returns_400() {
    Response resp = this.client.post("/");

    assertThat(resp.statusCode()).isEqualTo(StatusCode.BAD_REQUEST);
  }

  @Test
  public void create_short_url_and_expand() {
    String url = "http://google.com";
    ShortUrlRequest request = new ShortUrlRequest(url);

    Response resp = this.client.post("/", request);
    ShortUrl shortUrl = fromJson(resp.body().get(), ShortUrl.class);

    resp = this.client.get(format("/%s", shortUrl.short_url));
    shortUrl = fromJson(resp.body().get(), ShortUrl.class);

    assertThat(shortUrl.url).isEqualTo(url);
  }
}
