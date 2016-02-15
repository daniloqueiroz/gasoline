package gasoline.response;

import java.util.Optional;

import gasoline.http.StatusCode;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public interface Response {

  public StatusCode statusCode();

  public Optional<String> body();
}
