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
