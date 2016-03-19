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
