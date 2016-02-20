package gasoline;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import gasoline.http.StatusCode;
import gasoline.response.Response;
import gasoline.testing.TestingApp.Message;

public class ContextTest {

  @Test
  public void toJson_fromJson() {
    String content = "Hey na na!";
    String json = Context.toJson(new Message(content));
    Message msg = Context.fromJson(json, Message.class);

    assertThat(msg.content).isEqualTo(content);
  }

  @Test
  public void toJson_fromJsonResponse() {
    String content = "Hey na na!";
    Response resp = new Response(StatusCode.OK, new Message(content));

    Message msg = Context.fromJson(resp.body().get(), Message.class);

    assertThat(msg.content).isEqualTo(content);
  }
}
