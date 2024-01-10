package io.vividcode.onlinejudge.javarunner.service.converter;

public interface Converter<T> {

  T fromString(String input);

  String toString(T t);
}
