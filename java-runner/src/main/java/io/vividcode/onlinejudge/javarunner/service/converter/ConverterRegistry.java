package io.vividcode.onlinejudge.javarunner.service.converter;

import com.google.common.base.Splitter;
import io.vividcode.onlinejudge.javarunner.lib.TreeNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.assertj.core.util.Streams;
import org.springframework.stereotype.Service;

@Service
public class ConverterRegistry {

  private final Map<Class<?>, Converter<?>> converters = new HashMap<>();

  public ConverterRegistry() {
    converters.put(int.class, new IntConverter());
    converters.put(int[].class, new IntArrayConverter());
    converters.put(TreeNode.class, new TreeNodeConverter());
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<Converter<T>> findConverter(Class<?> type) {
    return Optional.ofNullable((Converter<T>) converters.get(type));
  }

  public Optional<String> tryConvertToString(Class<?> type, Object value) {
    return findConverter(type).map(converter -> converter.toString(value));
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> tryConvertFromString(Class<?> type, String input) {
    return findConverter(type).map(converter -> (T) converter.fromString(input));
  }

  static class IntConverter implements Converter<Integer> {

    @Override
    public Integer fromString(String input) {
      return Integer.parseInt(input);
    }

    @Override
    public String toString(Integer integer) {
      return Integer.toString(integer);
    }
  }

  // Format: [1,2,3,4,5]
  static class IntArrayConverter implements Converter<int[]> {

    @Override
    public int[] fromString(String input) {
      return Streams.stream(Splitter.on(",").omitEmptyStrings().trimResults().split(
          StringUtils.removeEnd(StringUtils.removeStart(input, "["), "]")
      )).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public String toString(int[] ints) {
      var arrays = Strings.join(IntStream.of(ints).iterator(), ',');
      return String.format("[%s]", arrays);
    }
  }

  static class TreeNodeConverter implements Converter<TreeNode> {

    @Override
    public TreeNode fromString(String input) {
      var values = Streams.stream(Splitter.on(",").omitEmptyStrings().trimResults().split(
          StringUtils.removeEnd(StringUtils.removeStart(input, "["), "]")
      )).map(value -> {
        if (Objects.equals(value, "null")) {
          return null;
        }
        return Integer.parseInt(value);
      }).toList();
      return TreeUtils.constructBinaryTree(values);
    }

    @Override
    public String toString(TreeNode treeNode) {
      return TreeUtils.toArray(treeNode);
    }
  }
}
