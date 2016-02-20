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
