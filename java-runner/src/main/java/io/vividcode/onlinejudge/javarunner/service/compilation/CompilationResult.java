package io.vividcode.onlinejudge.javarunner.service.compilation;

import java.util.List;
import org.springframework.util.CollectionUtils;

public record CompilationResult(String packageName, List<CompilationError> errors) {

  public boolean success() {
    return CollectionUtils.isEmpty(errors);
  }
}
