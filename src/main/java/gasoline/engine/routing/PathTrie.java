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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import gasoline.http.HttpMethod;

/**
 * Internal data structure used by the Router. This class is an implementation
 * of a PrefixTree.
 *
 * @author Danilo Queiroz <dpenna.queiroz@gmail.com>
 */
public class PathTrie {

  /**
   * Node structure
   */
  private class TrieNode {
    Map<String, TrieNode> children = new HashMap<>();
    String token;
    Route route;

    public TrieNode(String token) {
      this.token = token;
    }

    @Override
    public String toString() {
      return "TrieNode [token=" + this.token + ", route=" + this.route + ", children=" + this.children + "]";
    }
  }

  private final TrieNode root;

  protected PathTrie() {
    this.root = new TrieNode("ROOT");
  }

  /**
   * Insert an route to the {@link PathTrie}.
   */
  public void insert(String routePath, Route route) {
    TrieNode node = this.root;

    for (String token : this.getTokens(routePath)) {
      TrieNode child = null;
      if (node.children.containsKey(token)) {
        child = node.children.get(token);
      } else {
        child = new TrieNode(token);
        node.children.put(token, child);
      }
      node = child;
    }

    node.route = route;
  }

  /**
   * Search for a route that satisfy the given
   */
  public Route search(String urlPath, HttpMethod method) {
    Route result = null;
    TrieNode node = this.root;
    for (String token : this.getTokens(urlPath)) {
      for (Map.Entry<String, TrieNode> entry : node.children.entrySet()) {
        TrieNode current = entry.getValue();
        if (token.matches(entry.getKey())) {
          if (current.route == null || current.route.method == method) {
            node = entry.getValue();
            break;
          }
        }
      }
      result = node.route;
    }
    return result;

  }

  private String[] getTokens(String path) {
    String[] tokens = path.split("/");
    return tokens.length > 1 ? Arrays.copyOfRange(tokens, 1, tokens.length) : new String[0];
  }

  @Override
  public String toString() {
    return this.root.toString();
  }
}