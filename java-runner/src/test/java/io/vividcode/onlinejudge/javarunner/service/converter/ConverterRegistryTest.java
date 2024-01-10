package io.vividcode.onlinejudge.javarunner.service.converter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.vividcode.onlinejudge.javarunner.service.converter.ConverterRegistry.IntArrayConverter;
import io.vividcode.onlinejudge.javarunner.service.converter.ConverterRegistry.TreeNodeConverter;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ConverterRegistryTest {

  @ParameterizedTest
  @MethodSource("intArrays")
  void testIntArray(String input, int[] expectedOutput) {
    var converter = new IntArrayConverter();
    assertArrayEquals(expectedOutput, converter.fromString(input));
  }

  @ParameterizedTest
  @CsvSource(value = {
      "[1,null,2,3]",
      "[1]",
      "[]"
  }, delimiter = '#')
  void testTreeNode(String input) {
    var converter = new TreeNodeConverter();
    var treeNode = converter.fromString(input);
    assertEquals(input, converter.toString(treeNode));
  }

  static Stream<Object[]> intArrays() {
    return Stream.of(
        new Object[]{"[1,2,3]", new int[]{1, 2, 3}},
        new Object[]{"[1]", new int[]{1}},
        new Object[]{"[]", new int[0]}
    );
  }
}