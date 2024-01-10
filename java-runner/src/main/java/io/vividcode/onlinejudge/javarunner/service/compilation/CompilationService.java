package io.vividcode.onlinejudge.javarunner.service.compilation;

import io.vividcode.onlinejudge.javarunner.service.classloader.SolutionRunnerService;
import java.util.List;
import java.util.UUID;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompilationService {

  @Autowired
  SolutionRunnerService solutionRunnerService;

  private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

  public CompilationResult compile(String source) {
    var packageName = "z" + UUID.randomUUID().toString().replace("-", "");
    var actualSource = String.format(
        """
            package %s;
            import java.util.*;
            import io.vividcode.onlinejudge.javarunner.lib.*;
                        
            public class SolutionCreator {
              public static Solution create() {
                return new Solution();
              }
            }
                   
            %s    
            """
        , packageName, source);
    var output = solutionRunnerService.getClasses();
    var listener = new DiagnosticCollector<JavaFileObject>();
    var task = compiler.getTask(null, null, listener, List.of(
            "-d",
            output.toAbsolutePath().toString()
        ), null,
        List.of(new JavaSourceFromString("SolutionCreator", actualSource)));
    task.call();
    var errors = listener.getDiagnostics().stream()
        .filter(value -> value.getKind() == Kind.ERROR)
        .map(CompilationError::new)
        .toList();
    return new CompilationResult(packageName, errors);
  }
}
