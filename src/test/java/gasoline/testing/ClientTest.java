package gasoline.testing;

import static org.assertj.core.api.Assertions.assertThat;

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

}
