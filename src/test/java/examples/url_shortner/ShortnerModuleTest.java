package examples.url_shortner;

import static gasoline.Context.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import examples.url_shortner.ShortnerModule;
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
    Response resp = client.get("/inexistent");
    assertThat(resp.statusCode()).isEqualTo(StatusCode.NOT_FOUND);
  }
  
  @Test
  public void create_short_url_returns_201() {
    String url = "http://google.com";
    ShortUrlRequest request = new ShortUrlRequest(url);

    Response resp = client.post("/", request);
    ShortUrl shortUrl = fromJson(resp.body().get(), ShortUrl.class);

    assertThat(resp.statusCode()).isEqualTo(StatusCode.CREATED);
    assertThat(shortUrl.url).isEqualTo(url);
    assertThat(shortUrl.short_url).isNotEmpty();
  }

  @Test
  public void create_short_url_no_body_returns_400() {
    Response resp = client.post("/");

    assertThat(resp.statusCode()).isEqualTo(StatusCode.BAD_REQUEST);
  }

  @Test
  public void create_short_url_and_expand() {
    String url = "http://google.com";
    ShortUrlRequest request = new ShortUrlRequest(url);

    Response resp = client.post("/", request);
    ShortUrl shortUrl = fromJson(resp.body().get(), ShortUrl.class);

    resp = client.get(format("/%s", shortUrl.short_url));
    shortUrl = fromJson(resp.body().get(), ShortUrl.class);

    assertThat(shortUrl.url).isEqualTo(url);
  }
}
