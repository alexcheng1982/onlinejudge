package io.vividcode.onlinejudge.javarunner.model.result;

public record Success() implements Result {

  public static Success INSTANCE = new Success();

  @Override
  public boolean success() {
    return true;
  }
}
