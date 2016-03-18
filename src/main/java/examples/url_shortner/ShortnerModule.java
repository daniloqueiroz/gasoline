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

import static gasoline.Context.badRequest;
import static gasoline.Context.created;
import static gasoline.Context.fromJson;
import static java.lang.String.format;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import gasoline.Application;
import gasoline.Module;
import gasoline.utils.Log;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class ShortnerModule implements Module {

  private final HashMap<String, ShortUrl> shortUrls = new HashMap<>();
  private final AtomicInteger integer = new AtomicInteger(0);

  private String getRandomId() {
    return format("a%sd", this.integer.incrementAndGet());
  }

  private ShortUrl addShortUrl(String url) {
    String id = this.getRandomId();
    ShortUrl shortUrl = new ShortUrl(id, url);
    this.shortUrls.put(id, shortUrl);
    return shortUrl;
  }

  @Override
  public void init(Application app) {
    app.before((req) -> {
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
    }));
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
