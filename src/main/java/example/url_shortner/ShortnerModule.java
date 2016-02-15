package example.url_shortner;

import static gasoline.Context.badRequest;
import static gasoline.Context.created;
import static gasoline.Context.fromJson;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import gasoline.Application;
import gasoline.Module;
import gasoline.logging.Log;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class ShortnerModule implements Module {

  private HashMap<String, ShortUrl> shortUrls = new HashMap<>();
  private AtomicInteger integer = new AtomicInteger(0);

  private String getRandomId() {
    return format("a%sd", integer.incrementAndGet());
  }

  private ShortUrl addShortUrl(String url) {
    String id = this.getRandomId();
    ShortUrl shortUrl = new ShortUrl(id, url);
    this.shortUrls.put(id, shortUrl);
    return shortUrl;
  }

  public void init(Application app) {
    app.before(
        (req) -> {
          Log.info("Filter!");
        },

        app.post("/", (req) -> {
          Optional<String> body = req.body();
          if (body.isPresent()) {
            ShortUrlRequest in = fromJson(body.get(), ShortUrlRequest.class);
            return created(this.addShortUrl(in.url));
          } else {
            return badRequest();
          }
        }),

        app.get("/{short_url}", (req) -> {
          String shortUrl = req.attribute("short_url");
          return Optional.ofNullable(this.shortUrls.get(shortUrl));
        })
    );
  }

  public static class ShortUrl {
    public final String short_url;
    public final String url;

    public ShortUrl(String shortUrl, String url) {
      this.short_url = shortUrl;
      this.url = url;
    }
  }

  public static class ShortUrlRequest {
    public final String url;

    public ShortUrlRequest(String url) {
      this.url = url;
    }
  }

  public static void main(String[] args) {
    Application app = new Application(new ShortnerModule()::init);
    app.server().onPort(8080).start();
  }
}
