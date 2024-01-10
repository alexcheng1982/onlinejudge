package io.vividcode.onlinejudge.javarunner.service.compilation;

import java.util.List;

public record CompilationResult(String packageName, List<CompilationError> errors) {

  public boolean success() {
    return errors.isEmpty();
  }
}
