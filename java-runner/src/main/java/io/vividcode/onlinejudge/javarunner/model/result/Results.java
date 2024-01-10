package io.vividcode.onlinejudge.javarunner.model.result;

import java.util.List;
import org.springframework.util.CollectionUtils;

public record Results(List<? extends Result> results) {

  public boolean success() {
    if (CollectionUtils.isEmpty(results)) {
      return false;
    }
    return results.stream().allMatch(Result::success);
  }
}
