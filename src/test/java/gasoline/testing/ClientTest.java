package gasoline.testing;

import static gasoline.utils.Pair.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import gasoline.http.StatusCode;
import gasoline.response.Response;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class ClientTest {

  Client client = new Client(new TestingApp()::init);

  @Test
  public void get_noRoute_404() {
    Response resp = this.client.get("/not_found");
    assertThat(resp.statusCode()).isEqualTo(StatusCode.NOT_FOUND);
  }

  @Test
  public void get_withHeaders_200() {
    Map<String, String> headers = pairs(p("auth", "secret")).asMap();
    Response resp = this.client.get("/filtered/me", headers);
    assertThat(resp.statusCode()).isEqualTo(StatusCode.OK);
  }

}
