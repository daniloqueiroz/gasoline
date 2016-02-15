package gasoline.engine.routing;

import static gasoline.engine.routing.PathUtils.dynamicPathRegex;
import static gasoline.engine.routing.PathUtils.isDynamicPath;
import static gasoline.engine.routing.PathUtils.normalizePath;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class PathUtilsTest {
  @Test
  public void normalizePath_missingTrailingSlash_addsTrailingSlash() {
    String path = normalizePath("/test");
    assertThat(path).endsWith("/");
  }

  @Test
  public void normalizePath_mixedCase_putsPathToLowerCase() {
    String path = normalizePath("/uSErS/DANILO/");
    assertThat(path).isEqualTo("/users/danilo/");
  }

  @Test(expected = IllegalArgumentException.class)
  public void normalizePath_invalidChars_throwException() {
    normalizePath("/?/@/lala/");
  }

  @Test(expected = IllegalArgumentException.class)
  public void normalizePath_variableMissingBraces_throwException() {
    normalizePath("/users/{username/");
  }

  @Test(expected = IllegalArgumentException.class)
  public void normalizePath_variableIllegalChar_throwException() {
    normalizePath("/users/{user-name}/");
  }

  @Test
  public void normalizePath_with_variables_donotLowerCaseVariable() {
    String path = normalizePath("/USERS/{userName}/POSTS/{post_id}/");
    assertThat(path).isEqualTo("/users/{userName}/posts/{post_id}/");
  }

  @Test
  public void isDynamicPath_dynamicPath_true() {
    assertThat(isDynamicPath("/USERS/{userName}/POSTS/{id}/")).isTrue();
  }

  @Test
  public void isDynamicPath_staticPath_false() {
    assertThat(isDynamicPath("/USERS/POSTS/")).isFalse();
  }

  @Test
  public void dynamicPathRegex_matchesPath() {
    String pathRegex = dynamicPathRegex("/users/{userName}/posts/{id}/");
    assertThat("/users/danilo/posts/1/").matches(pathRegex);
  }

  @Test
  public void dynamicPathRegex_doesNotMatchesPath() {
    String pathRegex = dynamicPathRegex("/users/{userName}/");
    assertThat("/users/danilo/posts/1/").doesNotMatch(pathRegex);
  }
}
