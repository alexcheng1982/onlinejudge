package io.vividcode.onlinejudge.javarunner.service.converter;

import io.vividcode.onlinejudge.javarunner.lib.TreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class TreeUtils {

  public static TreeNode constructBinaryTree(List<Integer> treeValues) {
    if (CollectionUtils.isEmpty(treeValues)) {
      return null;
    }
    TreeNode root = new TreeNode(treeValues.get(0));
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    for (int i = 1; i < treeValues.size(); i++) {
      TreeNode curr = queue.poll();
      if (treeValues.get(i) != null) {
        curr.left = new TreeNode(treeValues.get(i));
        queue.offer(curr.left);
      }
      if (++i < treeValues.size() && treeValues.get(i) != null) {
        curr.right = new TreeNode(treeValues.get(i));
        queue.offer(curr.right);
      }
    }
    return root;
  }

  public static String toArray(TreeNode root) {
    if (root == null) {
      return "[]";
    }
    List<Integer> result = new ArrayList<>();
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
      TreeNode node = queue.poll();
      result.add(node != null ? node.val : null);
      if (node != null) {
        queue.offer(node.left);
        queue.offer(node.right);
      }
    }
    result = result.reversed().stream().dropWhile(Objects::isNull).toList().reversed();
    return "[" + result.stream().map(n -> n != null ? n.toString() : "null").collect(
        Collectors.joining(",")) + "]";
  }
}
