package io.vividcode.onlinejudge.javarunner.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vividcode.onlinejudge.javarunner.lib.TreeNode;
import io.vividcode.onlinejudge.javarunner.model.JavaEntryPoint;
import io.vividcode.onlinejudge.javarunner.model.JudgeInput;
import io.vividcode.onlinejudge.javarunner.model.Problem;
import io.vividcode.onlinejudge.javarunner.model.TestCase;
import io.vividcode.onlinejudge.javarunner.model.TestCaseMatchStrategy.IntArrayIgnoringOrderTestCaseMatchStrategy;
import io.vividcode.onlinejudge.javarunner.model.UserInput;
import io.vividcode.onlinejudge.javarunner.service.converter.ConverterRegistry;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JudgeServiceTest {

  @Autowired
  JudgeService judgeService;

  @Autowired
  ConverterRegistry converterRegistry;

  @Test
  @DisplayName("two sum - https://leetcode.cn/problems/two-sum")
  void twoSum() {
    var input = new JudgeInput(
        new Problem("twoSum",
            new JavaEntryPoint("Solution",
                "twoSum",
                List.of(int[].class, int.class),
                int[].class),
            List.of(
                new TestCase(List.of("[2,7,11,15]", "9"), "[1,0]",
                    new IntArrayIgnoringOrderTestCaseMatchStrategy(converterRegistry)),
                new TestCase(List.of("[3,2,4]", "6"), "[1,2]",
                    new IntArrayIgnoringOrderTestCaseMatchStrategy(converterRegistry))
            )),
        new UserInput("""
                        class Solution {
                             public int[] twoSum(int[] nums, int target) {
                                 String a = b + c;
                                 Map<Integer, Integer> hashtable = new HashMap<Integer, Integer>();
                                 for (int i = 0; i < nums.length; ++i) {
                                     if (hashtable.containsKey(target - nums[i])) {
                                         return new int[]{hashtable.get(target - nums[i]), i};
                                     }
                                     hashtable.put(nums[i], i);
                                 }
                                 return new int[0];
                             }
                         }
                      
            """)
    );
    var result = judgeService.judge(input);
    assertFalse(result.success());
  }

  @Test
  @DisplayName("climbing stairs - https://leetcode.cn/problems/climbing-stairs/")
  void climbStairs() {
    var input = new JudgeInput(
        new Problem("climbStairs",
            new JavaEntryPoint("Solution",
                "climbStairs",
                List.of(int.class),
                int.class), List.of(
            new TestCase(List.of("2"), "2", null),
            new TestCase(List.of("3"), "3", null)
        )),
        new UserInput("""
                        class Solution {
                            public int climbStairs(int n) {
                                int p = 0, q = 0, r = 1;
                                for (int i = 1; i <= n; ++i) {
                                    p = q;
                                    q = r;
                                    r = p + q;
                                }
                                return r;
                            }
                        }
            """)
    );
    var result = judgeService.judge(input);
    assertTrue(result.success());
  }

  @Test
  @DisplayName("single Number - https://leetcode.cn/problems/single-number/")
  void singleNumber() {
    var input = new JudgeInput(
        new Problem("singleNumber",
            new JavaEntryPoint("Solution",
                "singleNumber",
                List.of(int[].class),
                int.class), List.of(
            new TestCase(List.of("[2,2,1]"), "1", null),
            new TestCase(List.of("[4,1,2,1,2]"), "4", null)
        )),
        new UserInput("""
                        class Solution {
                             public int singleNumber(int[] nums) {
                                 int single = 0;
                                 for (int num : nums) {
                                     single ^= num;
                                 }
                                 return single;
                             }
                         }
            """)
    );
    var result = judgeService.judge(input);
    assertTrue(result.success());
  }

  @Test
  @DisplayName("binary-tree-inorder-traversal - https://leetcode.cn/problems/binary-tree-inorder-traversal/")
  void binaryTreeInorderTraversal() {
    var input = new JudgeInput(
        new Problem("binaryTreeInorderTraversal",
            new JavaEntryPoint("Solution",
                "inorderTraversal",
                List.of(TreeNode.class),
                int[].class), List.of(
            new TestCase(List.of("[1,null,2,3]"), "[1,3,2]", null),
            new TestCase(List.of("[1]"), "[1]", null)
        )),
        new UserInput("""
                        class Solution {
                              public int[] inorderTraversal(TreeNode root) {
                                  List<Integer> res = new ArrayList<Integer>();
                                  inorder(root, res);
                                  return res.stream().mapToInt(i -> i).toArray();
                              }
                          
                              public void inorder(TreeNode root, List<Integer> res) {
                                  if (root == null) {
                                      return;
                                  }
                                  inorder(root.left, res);
                                  res.add(root.val);
                                  inorder(root.right, res);
                              }
                          }
            """)
    );
    var result = judgeService.judge(input);
    assertTrue(result.success());
  }
}