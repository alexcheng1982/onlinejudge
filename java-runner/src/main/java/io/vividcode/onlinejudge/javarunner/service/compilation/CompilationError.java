package io.vividcode.onlinejudge.javarunner.service.compilation;

import java.util.Locale;
import javax.tools.Diagnostic;

public record CompilationError(long lineNumber, String message) {

  public CompilationError(Diagnostic<?> diagnostic) {
    this(diagnostic.getLineNumber(), diagnostic.getMessage(Locale.ENGLISH));
  }
}
