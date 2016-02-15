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
