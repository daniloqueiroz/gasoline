package gasoline.response;

import java.util.Optional;

import gasoline.Context;
import gasoline.http.StatusCode;

public class RawResponse implements Response {
  private final StatusCode code;
  private Optional<String> body;

  public RawResponse(StatusCode code) {
    this(code, null);
  }

  public RawResponse(StatusCode code, Object content) {
    this.code = code;
    this.prepareBody(content);
  }

  @Override
  public Optional<String> body() {
    return this.body;
  }

  @Override
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
