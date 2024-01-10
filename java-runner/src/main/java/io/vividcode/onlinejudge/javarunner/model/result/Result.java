package io.vividcode.onlinejudge.javarunner.model.result;

public sealed interface Result permits
    Success,
    CompilationFailed,
    CodeRuntimeError,
    TestCaseFailed,
    TimeLimitExceeded {

  default boolean success() {
    return false;
  }
}
