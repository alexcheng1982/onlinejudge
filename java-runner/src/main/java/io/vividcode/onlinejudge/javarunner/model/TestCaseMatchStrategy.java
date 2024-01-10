package io.vividcode.onlinejudge.javarunner.model;

import io.vividcode.onlinejudge.javarunner.service.converter.ConverterRegistry;
import java.util.Arrays;
import java.util.Objects;

public interface TestCaseMatchStrategy {

  boolean match(String expected, Object actual);

  class DefaultTestCaseMatchStrategy implements TestCaseMatchStrategy {

    private final ConverterRegistry converterRegistry;
    private final Class<?> returnType;

    public DefaultTestCaseMatchStrategy(ConverterRegistry converterRegistry,
        Class<?> returnType) {
      this.converterRegistry = converterRegistry;
      this.returnType = returnType;
    }

    @Override
    public boolean match(String expected, Object actual) {
      String resultString = converterRegistry.tryConvertToString(
              returnType, actual)
          .orElse("");
      return Objects.equals(resultString, expected);
    }
  }

  class IntArrayIgnoringOrderTestCaseMatchStrategy implements
      TestCaseMatchStrategy {

    private final ConverterRegistry converterRegistry;

    public IntArrayIgnoringOrderTestCaseMatchStrategy(
        ConverterRegistry converterRegistry) {
      this.converterRegistry = converterRegistry;
    }

    @Override
    public boolean match(String expected, Object actual) {
      int[] expectedArray = (int[]) converterRegistry.tryConvertFromString(int[].class,
              expected)
          .orElse(new int[0]);
      int[] actualArray = (int[]) actual;
      Arrays.sort(expectedArray);
      Arrays.sort(actualArray);
      return Arrays.equals(expectedArray, actualArray);
    }
  }
}
