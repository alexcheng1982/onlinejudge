package io.vividcode.onlinejudge.javarunner.model.result;

import io.vividcode.onlinejudge.javarunner.service.compilation.CompilationError;
import java.util.List;

public record CompilationFailed(List<CompilationError> errors) implements Result {

}
